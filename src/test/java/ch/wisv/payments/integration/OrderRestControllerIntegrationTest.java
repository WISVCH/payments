package ch.wisv.payments.integration;

import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderRequest;
import ch.wisv.payments.model.OrderResponse;
import ch.wisv.payments.model.Product;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRestControllerIntegrationTest extends RestIntegrationTest {

    @Test
    public void testRequestPayment() {
        Product product = addProduct();
        OrderRequest request = new OrderRequest();
        request.setEmail("test@mail.com");
        request.setName("Test Name");
        request.setProductKeys(Collections.singletonList(product.getKey()));

        ResponseEntity<OrderResponse> responseEntity = restTemplate.postForEntity("/api/orders", request, OrderResponse.class);

        Mockito.verify(paymentService, Mockito.atLeastOnce()).registerOrder(Mockito.any(Order.class));
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }
}