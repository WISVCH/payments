package ch.wisv.payments.rest.eventsync;

import ch.wisv.payments.admin.committees.CommitteeRepository;
import ch.wisv.payments.exception.ProductNotFoundException;
import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.CommitteeEnum;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.model.eventsync.ProductEventsSync;
import ch.wisv.payments.rest.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class EventsSyncProductServiceImpl implements EventsSyncProductService {

    /**
     * ProductRepository.
     */
    private final ProductRepository productRepository;

    /**
     * CommitteeRepository.
     */
    private final CommitteeRepository committeeRepository;

    /**
     * Constructor.
     *
     * @param productRepository   of type ProductRepository
     * @param committeeRepository of type CommitteeRepository
     */
    public EventsSyncProductServiceImpl(ProductRepository productRepository, CommitteeRepository committeeRepository) {
        this.productRepository = productRepository;
        this.committeeRepository = committeeRepository;
    }

    /**
     * @param productEventsSync of type ProductEventsSync
     */
    @Override
    public void createProduct(ProductEventsSync productEventsSync) {
        Product product = new Product();
        product.setKey(productEventsSync.getKey());

        this.changeProductValues(product, productEventsSync);

        productRepository.saveAndFlush(product);
    }

    /**
     * @param productEventsSync of type ProductEventsSync
     */
    @Override
    public void editProduct(ProductEventsSync productEventsSync) {
        Product product = this.getProductByKey(productEventsSync.getKey());

        this.changeProductValues(product, productEventsSync);

        productRepository.saveAndFlush(product);
    }

    /**
     * Change the Product values using a ProductEventsSync model.
     *
     * @param product           of type Product.
     * @param productEventsSync of type ProductEventsSync.
     */
    private void changeProductValues(Product product, ProductEventsSync productEventsSync) {
        if (product.getCommittee() == null || product.getCommittee().toString().equals(productEventsSync.getOrganizedBy())) {
            this.changeProductCommittee(product, productEventsSync);
        }

        product.setDescription(productEventsSync.getDescription());
        product.setName(productEventsSync.getTitle());
        product.setPrice(productEventsSync.getPrice().floatValue());
    }

    /**
     * Change the Committee of Product.
     *
     * @param product           of type Product
     * @param productEventsSync of type ProductEventsSync
     */
    private void changeProductCommittee(Product product, ProductEventsSync productEventsSync) {
        CommitteeEnum committeeEnum = CommitteeEnum.valueOf(productEventsSync.getOrganizedBy());
        int year = 2017;

        Committee committee = committeeRepository.findOneByNameAndYear(committeeEnum, year)
                .orElse(new Committee(committeeEnum, year));

        if (committee.getId() == 0) {
            committeeRepository.saveAndFlush(committee);
        }

        product.setCommittee(committee);
    }

    /**
     * @param productEventsSync of type ProductEventsSync
     */
    @Override
    public void deleteProduct(ProductEventsSync productEventsSync) {
        Product product = this.getProductByKey(productEventsSync.getKey());

        productRepository.delete(product);
    }

    /**
     * Get a Product by its key.
     *
     * @param key of type Key
     * @return Product
     */
    private Product getProductByKey(String key) {
        return productRepository.findOneByKey(key).orElseThrow(ProductNotFoundException::new);
    }
}
