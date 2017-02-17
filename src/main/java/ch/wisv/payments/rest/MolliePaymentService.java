package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderResponse;
import ch.wisv.payments.model.OrderStatus;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.rest.repository.OrderRepository;
import nl.stil4m.mollie.Client;
import nl.stil4m.mollie.ClientBuilder;
import nl.stil4m.mollie.ResponseOrError;
import nl.stil4m.mollie.domain.CreatePayment;
import nl.stil4m.mollie.domain.CreatedPayment;
import nl.stil4m.mollie.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MolliePaymentService implements PaymentService {

    private OrderRepository orderRepository;
    private final MailService mailService;

    private Client mollie;

    @Value("${payments.paymentReturnUrl}")
    String returnUrl;

    @Autowired
    public MolliePaymentService(OrderRepository orderRepository, @Value("${payments.molliekey:null}") String apiKey, MailService mailService) {
        this.orderRepository = orderRepository;
        this.mollie = new ClientBuilder().withApiKey(apiKey).build();
        this.mailService = mailService;
    }

    public OrderResponse registerOrder(Order order) {
        Map<String, Object> metadata = new HashMap<>();

        String method = "ideal";

        if (order.getReturnURL() != null) {
            returnUrl = order.getReturnURL();
        }

        double amount = order.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        amount += 0.29;

        CreatePayment payment = new CreatePayment(method, amount, "W.I.S.V. 'Christiaan Huygens' Payments",
                returnUrl + "?reference=" + order.getPublicReference(), metadata);

        //First try is for IOExceptions coming from the Mollie Client.
        try {
            // Create the payment over at Mollie
            ResponseOrError<CreatedPayment> molliePayment = mollie.payments().create(payment);

            if (molliePayment.getSuccess()) {
                // All good, update the order
                updateOrder(order, molliePayment);
                return new OrderResponse(order);
            } else {
                // Mollie returned an error.
                handleMollieError(molliePayment);
                return null;
            }
        } catch (IOException e) {
            // This indicates the HttpClient encountered some error
            throw new RuntimeException("Could not connect to the Paymentprovider");
        }
    }

    private void updateOrder(Order order, ResponseOrError<CreatedPayment> molliePayment) {
        // Insert the Mollie ID for future providerReference
        order.setProviderReference(molliePayment.getData().getId());
        order.setPaymentURL(molliePayment.getData().getLinks().getPaymentUrl());
        order.setStatus(OrderStatus.WAITING);

        // Save the changes to the order
        orderRepository.save(order);
    }

    @Override
    public Order updateStatus(String orderReference) {
        Order order = orderRepository.findByPublicReference(orderReference)
                .orElseThrow(() -> new RuntimeException("Order with providerReference " + orderReference + " not found"));

        // This try is for the Mollie API internal HttpClient
        try {
            // Request a payment from Mollie
            ResponseOrError<Payment> paymentResponseOrError = mollie.payments().get(order.getProviderReference());

            // If the request was a success, we can update the order
            if (paymentResponseOrError.getSuccess()) {
                // There are a couple of possible statuses. Enum would have been nice. We select a couple of relevant
                // statuses to translate to our own status.
                switch (paymentResponseOrError.getData().getStatus()) {
                    case "pending": {
                        order.setStatus(OrderStatus.WAITING);
                        break;
                    }
                    case "cancelled": {
                        order.setStatus(OrderStatus.CANCELLED);
                        break;
                    }
                    case "expired": {
                        order.setStatus(OrderStatus.EXPIRED);
                        break;
                    }
                    case "paid": {
                        order.setStatus(OrderStatus.PAID);
                        mailService.sendOrderConfirmation(order);
                        break;
                    }
                    case "paidout": {
                        order.setStatus(OrderStatus.PAID);
                        break;
                    }
                }
                return orderRepository.save(order);
            } else {
                // Order status could not be updated for some reason. Return the original order
                handleMollieError(paymentResponseOrError);
                return order;
            }
        } catch (IOException e) {
            // This indicates the HttpClient encountered some error
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleMollieError(ResponseOrError<?> mollieResponseWithError) {
        // Some error occured, but connection to Mollie succeeded, which means they have something to say.
        Map molliePaymentError = mollieResponseWithError.getError();

        // Make the compiler shut up, this is something stupid in the Mollie API Client
        Map error = (Map) molliePaymentError.get("error");
        throw new RuntimeException((String) error.get("message"));
    }
}