package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderRequest;
import ch.wisv.payments.model.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderRestController {

    private PaymentService paymentService;
    private OrderService orderService;

    @Autowired
    public OrderRestController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> requestPayment(@Validated @RequestBody OrderRequest request) {

        Order order = orderService.createOrderFromRequest(request);

        OrderResponse response = paymentService.registerOrder(order);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{orderReference}")
    public ResponseEntity<?> getOrderStatus(@PathVariable String orderReference) {
        Order order = orderService.getOrderByPublicReference(orderReference);

        return new ResponseEntity<>(new OrderResponse(order), HttpStatus.OK);
    }
}
