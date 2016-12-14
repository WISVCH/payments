package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;

public interface MailService {

    void sendOrderConfirmation(Order order);
}
