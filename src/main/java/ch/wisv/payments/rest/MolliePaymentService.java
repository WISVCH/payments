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
import nl.stil4m.mollie.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MolliePaymentService implements PaymentService {

    private OrderRepository orderRepository;
    private final MailService mailService;

    private Client mollie;

    @Value("${payments.returnUrl}")
    String returnUrl;

    @Value("${payments.webhookUrl}")
    String webhookUrl;

    @Autowired
    public MolliePaymentService(OrderRepository orderRepository, @Value("${payments.molliekey:null}") String apiKey, MailService mailService) {
        this.orderRepository = orderRepository;
        this.mollie = new ClientBuilder().withApiKey(apiKey).build();
        this.mailService = mailService;
    }

    public OrderResponse registerOrder(Order order) {
        Map<String, Object> metadata = new HashMap<>();

        Optional<String> method = Optional.of(order.getMethod().getName());

        if (order.getReturnURL() != null) {
            returnUrl = order.getReturnURL();
        }

        double amount = order.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        amount = order.getMethod().calculateCostIncludingTransaction(amount);

        CreatePayment payment = new CreatePayment(method, amount, "W.I.S.V. 'Christiaan Huygens' Payments",
                returnUrl + "?reference=" + order.getPublicReference(), Optional.of(webhookUrl), metadata);

        //First try is for IOExceptions coming from the Mollie Client.
        try {
            // Create the payment over at Mollie
            ResponseOrError<Payment> molliePayment = mollie.payments().create(payment);

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

    private void updateOrder(Order order, ResponseOrError<Payment> molliePayment) {
        // Insert the Mollie ID for future providerReference
        order.setProviderReference(molliePayment.getData().getId());
        order.setStatus(OrderStatus.WAITING);
        order.setPaymentURL(molliePayment.getData().getLinks().getPaymentUrl());

        // Save the changes to the order
        orderRepository.saveAndFlush(order);
    }

    @Override
    public Order updateStatusByPublicReference(String publicOrderReference) {
        Order order = orderRepository.findByPublicReference(publicOrderReference)
                .orElseThrow(() -> new RuntimeException("Order with publicReference " + publicOrderReference + " not found"));
        return updateOrder(order);
    }

    @Override
    public Order updateStatusByProviderReference(String providerOrderReference) {
        Order order = orderRepository.findByProviderReference(providerOrderReference)
                .orElseThrow(() -> new RuntimeException("Order with providerReference " + providerOrderReference + " not found"));
        return updateOrder(order);
    }

    private Order updateOrder(Order order) {
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
                        if (!order.getStatus().equals(OrderStatus.PAID) && order.isMailConfirmation()) {
                            mailService.sendOrderConfirmation(order);
                        }
                        order.setStatus(OrderStatus.PAID);
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
