package ch.wisv.payments.rest;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentMethod;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderResponse;
import ch.wisv.payments.model.OrderStatus;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.rest.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

        PaymentMethod method;

        if(order.getMethod().getName().equals("ideal")){
            method = PaymentMethod.IDEAL;

        } else{
            method = PaymentMethod.SOFORT;
        }


        if (order.getReturnURL() != null) {
            returnUrl = order.getReturnURL();
        }

        double value = order.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        value = order.getMethod().calculateCostIncludingTransaction(value);
        Amount paymentAmount = Amount.builder().value(BigDecimal.valueOf(value)).currency("EUR").build();
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .method(Optional.of(List.of(method)))
                .amount(paymentAmount)
                .description("W.I.S.V. 'Christiaan Huygens' Payments")
                .redirectUrl(Optional.of(returnUrl + "?reference=" + order.getPublicReference()))
                .webhookUrl(Optional.of(webhookUrl))
                .metadata(metadata)
                .build();

        //First try is for IOExceptions coming from the Mollie Client.
        try {
            // Create the payment over at Mollie
            PaymentResponse molliePayment = mollie.payments().createPayment(paymentRequest);

            // All good, update the order
            updateOrder(order, molliePayment);
            return new OrderResponse(order);

        } catch(MollieException e) {
            handleMollieError(e);
            return null;
        }
    }

    private void updateOrder(Order order, PaymentResponse molliePayment) {
        // Insert the Mollie ID for future providerReference
        order.setProviderReference(molliePayment.getId());
        order.setStatus(OrderStatus.WAITING);
        order.setPaymentURL(molliePayment.getLinks().getCheckout().getHref());

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
            PaymentResponse paymentResponse = mollie.payments().getPayment(order.getProviderReference());

            // There are a couple of possible statuses. Enum would have been nice. We select a couple of relevant
            // statuses to translate to our own status.

            switch (paymentResponse.getStatus()) {
                case PENDING: {
                    order.setStatus(OrderStatus.WAITING);
                    break;
                }
                case CANCELED: {
                    order.setStatus(OrderStatus.CANCELLED);
                    break;
                }
                case EXPIRED: {
                    order.setStatus(OrderStatus.EXPIRED);
                    break;
                }
                case PAID: {
                    if (!order.getStatus().equals(OrderStatus.PAID) && order.isMailConfirmation()) {
                        mailService.sendOrderConfirmation(order);
                    }
                    order.setStatus(OrderStatus.PAID);
                    break;
                }
            }
            return orderRepository.save(order);
        } catch (MollieException e) {
            handleMollieError(e);
            return order;
        }
    }

    private void handleMollieError(MollieException mollieException) {
        // Some error occured, but connection to Mollie succeeded, which means they have something to say.
        Map molliePaymentError = mollieException.getDetails();

        // Make the compiler shut up, this is something stupid in the Mollie API Client
        Map error = (Map) molliePaymentError.get("error");
        throw new RuntimeException((String) error.get("message"));
    }
}