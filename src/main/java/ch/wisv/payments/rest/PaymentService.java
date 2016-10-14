package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderResponse;

/**
 * This service converts a request to an Order class, which we can store. After this, the order is sent to a provider
 * service which handles the actual payment
 */
public interface PaymentService {

    OrderResponse registerOrder(Order order);

    Order updateStatus(String orderReference);

}