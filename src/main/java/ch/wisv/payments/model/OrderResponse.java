package ch.wisv.payments.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderResponse {

    String url;
    String publicReference;
    String status;

    public OrderResponse(Order order) {
        this.url = order.getPaymentURL();
        this.publicReference = order.getPublicReference();
        this.status = order.getStatus().toString();
    }
}