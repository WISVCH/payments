package ch.wisv.payments.unit;

import ch.wisv.payments.exception.EmptyOrderException;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderRequest;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.rest.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceImplTest extends ServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    public void createOrderFromRequestSingleProduct() {
        Product product = persistAndGetProduct();

        OrderRequest request = new OrderRequest();
        request.setName("name");
        request.setEmail("name@mail.com");
        request.setProductKeys(Collections.singletonList(product.getKey()));

        Order order = orderService.createOrderFromRequest(request);

        assertThat(order.getProducts()).contains(product);
    }

    @Test
    public void createOrderFromRequestNoProducts() {
        OrderRequest request = new OrderRequest();
        request.setName("name");
        request.setEmail("name@mail.com");
        request.setProductKeys(Collections.emptyList());

        thrown.expect(EmptyOrderException.class);
        orderService.createOrderFromRequest(request);
    }

    @Test
    public void getOrderByPublicReference() {
    }

    @Test
    public void getOrderByProviderReference() {
    }

    @Test
    public void getAllOrders() {
    }

    @Test
    public void getOrdersByProductId() {
    }

    @Test
    public void getOrdersByCommittee() {
    }

    @Test
    public void getTransactionCostByCommittee() {
    }

}