package ch.wisv.payments.rest;

import ch.wisv.payments.RestIntegrationTest;
import ch.wisv.payments.rest.repository.OrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderRestControllerIntegrationTest extends RestIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void requestPaymentSingleProduct() {

    }

    @Test
    public void requestPaymentSingleProductInGroup() {

    }

    @Test
    public void requestPaymentSingleNonExistingProduct() {

    }

    @Test
    public void requestPaymentMultipleProductInSameGroup() {

    }

    @Test
    public void requestPaymentMultipleProductDifferentGroups() {

    }

    @Test
    public void requestPaymentMultipleProductsOneInGroup() {

    }

    @Test
    public void requestPaymentMultipleProductsOneNonExistent() {

    }

    @Test
    public void requestPaymentEmptyName() throws Exception {

    }

    @Test
    public void requestPaymentEmptyEmail() throws Exception {

    }

    @Test
    public void requestPaymentInvalidEmail() throws Exception {

    }

    @Test
    public void getOrderStatus() throws Exception {

    }

}