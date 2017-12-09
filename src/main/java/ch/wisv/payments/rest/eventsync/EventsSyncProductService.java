package ch.wisv.payments.rest.eventsync;

import ch.wisv.payments.model.eventsync.ProductEventsSync;

public interface EventsSyncProductService {

    /**
     * Create a Product from a ProductEventsSync model.
     *
     * @param productEventsSync of type ProductEventsSync
     */
    void createProduct(ProductEventsSync productEventsSync);

    /**
     * Edit a Product from a ProductEventsSync model.
     *
     * @param productEventsSync of type ProductEventsSync
     */
    void editProduct(ProductEventsSync productEventsSync);

    /**
     * Delete a Product from a ProductEventsSync model.
     *
     * @param productEventsSync of type ProductEventsSync
     */
    void deleteProduct(ProductEventsSync productEventsSync);
}
