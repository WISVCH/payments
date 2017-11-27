package ch.wisv.payments.model.eventsync;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ProductEventsSync extends EventsSync {

    /**
     * Price of the product.
     */
    private Double price;

    /**
     * Description of the product.
     */
    private String description;

    /**
     * Title of the product.
     */
    private String title;

    /**
     * UUID of the product.
     */
    private String key;

    /**
     * Enum string of the organizing Committee.
     */
    private String organizedBy;

}
