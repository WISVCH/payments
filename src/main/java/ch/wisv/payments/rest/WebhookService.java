package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;

public interface WebhookService {

    /**
     * Notify ... of a OrderStatus change.
     *
     * @param order of type Order
     */
    void sendOrderStatusChangeNotification(Order order);
}
