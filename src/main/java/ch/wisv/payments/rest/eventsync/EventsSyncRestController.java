package ch.wisv.payments.rest.eventsync;

import ch.wisv.payments.admin.products.ProductService;
import ch.wisv.payments.exception.ProductNotFoundException;
import ch.wisv.payments.model.eventsync.ProductEventsSync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.nio.charset.Charset;
import java.util.Base64;

import static ch.wisv.payments.util.ResponseEntityBuilder.createResponseEntity;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/chevents/sync")
@Validated
public class EventsSyncRestController {

    @Value("${events.username}")
    @NotNull
    private String username;

    @Value("${events.password}")
    @NotNull
    private String password;

    /**
     * ProductService.
     */
    private final ProductService productService;

    private final EventsSyncProductService eventsSyncProductService;

    /**
     * Constructor ProductService.
     *
     * @param productService of type ProductService
     * @param eventsSyncProductService of type EventsSyncProductService
     */
    public EventsSyncRestController(ProductService productService, EventsSyncProductService eventsSyncProductService) {
        this.productService = productService;
        this.eventsSyncProductService = eventsSyncProductService;
    }


    @PostMapping("/product/")
    public ResponseEntity<?> eventSync(HttpServletRequest request, @RequestBody ProductEventsSync productEventsSync) {
        String[] credentials = this.decryptBasicAuthHeader(request.getHeader("Authorization"));

        if (!this.authChEvents(credentials)) {
            return createResponseEntity(HttpStatus.UNAUTHORIZED, "User is not authorized", null);
        }

        switch (productEventsSync.getTrigger()) {
            case "PRODUCT_CREATE_EDIT":
                this.determineCreateOrUpdate(productEventsSync);
                break;
            case "PRODUCT_DELETE":
                eventsSyncProductService.deleteProduct(productEventsSync);
                break;
            default:
                return createResponseEntity(HttpStatus.BAD_REQUEST, "Events trigger not supported!", null);
        }

        return createResponseEntity(HttpStatus.OK, null, null);
    }

    /**
     * Check if user is CH Events.
     *
     * @param credentials of type String[]
     */
    private boolean authChEvents(String[] credentials) {
        return credentials[0].equals(this.username) && credentials[1].equals(this.password);
    }

    /**
     * Determine if a Product should be updated or created.
     *
     * @param productEventsSync of type ProductEventsSync
     */
    private void determineCreateOrUpdate(ProductEventsSync productEventsSync) {
        try {
            productService.getProductByKey(productEventsSync.getKey());

            eventsSyncProductService.editProduct(productEventsSync);
        } catch (ProductNotFoundException e) {
            eventsSyncProductService.createProduct(productEventsSync);
        }
    }

    /**
     * Decrypt the Basic Auth header.
     *
     * @param basicAuth of type String
     * @return String[] with username and password
     */
    private String[] decryptBasicAuthHeader(String basicAuth) {
        String base64Credentials = basicAuth.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));

        return credentials.split(":", 2);
    }
}
