package ch.wisv.payments.rest;

import ch.wisv.payments.RestIntegrationTest;
import ch.wisv.payments.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

import static junit.framework.TestCase.*;

@ActiveProfiles("test")
public class EventsSyncRestControllerIntegrationTest extends RestIntegrationTest {

    private final String PRODUCT_KEY = "1dc5dcf3-1d42-4ff5-ac97-bac899b7d12f";
    private final String PRODUCT_NAME = "MatCH ticket";
    private final Double PRODUCT_PRICE = 3.0;
    private final String PRODUCT_DESCRIPTION = "Ticket for the MatCH activity.";
    private final String PRODUCT_COMMITTEE = "MATCH";

    private final String REQUEST_BODY_CREATE_EDIT = "{\"trigger\": \"PRODUCT_CREATE_EDIT\",\"price\": " + PRODUCT_PRICE + ",\"description\": \"" + PRODUCT_DESCRIPTION + "\"," +
            "\"title\":\"" + PRODUCT_NAME + "\",\"key\":\"" + PRODUCT_KEY + "\",\"organizedBy\": \"" + PRODUCT_COMMITTEE + "\"}";
    private final String REQUEST_BODY_DELETE = "{\"trigger\": \"PRODUCT_DELETE\",\"key\":\"" + PRODUCT_KEY + "\"}";
    private final String REQUEST_BODY_NOT_SUPPORTED_TRIGGER = "{\"trigger\": \"FAILED_TRIGGER\",\"price\": " + PRODUCT_PRICE + "," +
            "\"description\": \"" + PRODUCT_DESCRIPTION + "\",\"title\":\"" + PRODUCT_NAME + "\",\"key\":\"" + PRODUCT_KEY + "\",\"organizedBy\": \"" + PRODUCT_COMMITTEE + "\"}";

    @Test
    public void testBasicAuthFails() {
        String credentials = new String(Base64.getEncoder().encode("sven:popping".getBytes()),
                Charset.forName("UTF-8"));

        this.sendRequest(credentials, REQUEST_BODY_CREATE_EDIT, HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testCreateProductSyncCreate() {
        String credentials = new String(Base64.getEncoder().encode("CH Events:secret".getBytes()),
                Charset.forName("UTF-8"));

        // Check if product does not already exists.
        assertFalse(productRepository.findOneByKey(PRODUCT_KEY).isPresent());
        this.sendRequest(credentials, REQUEST_BODY_CREATE_EDIT, HttpStatus.SC_OK);

        Optional<Product> optional = productRepository.findOneByKey(PRODUCT_KEY);
        assertTrue(optional.isPresent());

        Product product = optional.get();
        assertEquals(PRODUCT_NAME, product.getName());
        assertEquals(PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(PRODUCT_PRICE.floatValue(), product.getPrice());
        assertEquals(PRODUCT_COMMITTEE, product.getCommittee().getName().toString());
    }

    @Test
    public void testCreateProductSyncUpdate() {
        String credentials = new String(Base64.getEncoder().encode("CH Events:secret".getBytes()),
                Charset.forName("UTF-8"));

        this.sendRequest(credentials, REQUEST_BODY_CREATE_EDIT, HttpStatus.SC_OK);

        Optional<Product> optional = productRepository.findOneByKey(PRODUCT_KEY);
        assertTrue(optional.isPresent());

        Product product = optional.get();
        int productId = product.getId();
        String productKey = product.getKey();

        this.sendRequest(credentials, REQUEST_BODY_CREATE_EDIT, HttpStatus.SC_OK);

        optional = productRepository.findOneByKey(PRODUCT_KEY);
        assertTrue(optional.isPresent());
        Product product2 = optional.get();

        assertEquals(productId, product2.getId());
        assertEquals(productKey, product2.getKey());
    }

    @Test
    public void testCreateProductSyncDelete() {
        String credentials = new String(Base64.getEncoder().encode("CH Events:secret".getBytes()),
                Charset.forName("UTF-8"));

        this.sendRequest(credentials, REQUEST_BODY_DELETE, HttpStatus.SC_OK);

        assertFalse(productRepository.findOneByKey(PRODUCT_KEY).isPresent());
    }

    @Test
    public void testTriggerNotSupported() {
        String credentials = new String(Base64.getEncoder().encode("CH Events:secret".getBytes()),
                Charset.forName("UTF-8"));

        this.sendRequest(credentials, REQUEST_BODY_NOT_SUPPORTED_TRIGGER, HttpStatus.SC_BAD_REQUEST);

        assertFalse(productRepository.findOneByKey(PRODUCT_KEY).isPresent());
    }

    private void sendRequest(String credentials, String body, int httpStatusCode) {
        //@formatter:off
        RestAssured.given().
            body(body).
        when().
            header(new Header("Authorization", "Basic " + credentials)).
            header(new Header("Content-Type", "application/json")).
            post("/payments/api/chevents/sync/product/").
        then().
            statusCode(httpStatusCode);
        // @formatter:on
    }
}