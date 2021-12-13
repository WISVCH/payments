package ch.wisv.payments.rest;

import ch.wisv.payments.RestIntegrationTest;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderRequest;
import ch.wisv.payments.model.OrderResponse;
import ch.wisv.payments.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
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

    @Test
    public void testRequestPaymentWithMailConfirmation() {
        Product product = this.addProduct();

        //@formatter:off
        RestAssured.given().
            body("{\"productKeys\": [\"" + product.getKey() + "\"],\"name\": \"Sven Popping\",\"email\": \"svenp@ch.tudelft.nl\"," +
                    "\"returnUrl\":\"http://return.url/order/complete/\",\"mailConfirmation\": true}").
        when().
            header(new Header("Content-Type", "application/json")).
            post("/payments/api/orders").
        then().
            statusCode(HttpStatus.CREATED.value());
        // @formatter:on
    }

    @Test
    public void testRequestPaymentWithoutMailConfirmation() {
        Product product = this.addProduct();
        //@formatter:off
        RestAssured.given().
            body("{\"productKeys\": [\"" + product.getKey() + "\"],\"name\": \"Sven Popping\",\"email\": \"svenp@ch.tudelft.nl\"," +
                    "\"returnUrl\":\"http://return.url/order/complete/\",\"mailConfirmation\": false}").
        when().
            header(new Header("Content-Type", "application/json")).
            post("/payments/api/orders").
        then().
            statusCode(HttpStatus.CREATED.value());
        // @formatter:on
    }

    @Test
    public void testRequestPaymentOptionalMailConfirmation() {
        Product product = this.addProduct();
        //@formatter:off
        RestAssured.given().
            body("{\"productKeys\": [\"" + product.getKey() + "\"],\"name\": \"Sven Popping\",\"email\": \"svenp@ch.tudelft.nl\"," +
                    "\"returnUrl\":\"http://return.url/order/complete/\"}").
        when().
            header(new Header("Content-Type", "application/json")).
            post("/payments/api/orders").
        then().
            statusCode(HttpStatus.CREATED.value());
        // @formatter:on
    }
}