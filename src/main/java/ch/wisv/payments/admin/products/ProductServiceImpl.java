package ch.wisv.payments.admin.products;

import ch.wisv.payments.admin.committees.CommitteeService;
import ch.wisv.payments.admin.products.request.ProductGroupRequest;
import ch.wisv.payments.admin.products.request.ProductRequest;
import ch.wisv.payments.exception.ProductGroupInUseException;
import ch.wisv.payments.exception.ProductInUseException;
import ch.wisv.payments.exception.ProductNotFoundException;
import ch.wisv.payments.model.*;
import ch.wisv.payments.rest.OrderService;
import ch.wisv.payments.rest.repository.ProductGroupRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductGroupRepository productGroupRepository;
    private CommitteeService committeeService;
    private OrderService orderService;

    public ProductServiceImpl(ProductRepository productRepository, ProductGroupRepository productGroupRepository, CommitteeService committeeService, OrderService orderService) {
        this.productRepository = productRepository;
        this.productGroupRepository = productGroupRepository;
        this.committeeService = committeeService;
        this.orderService = orderService;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void addProduct(ProductRequest productRequest) {
        Committee committee = committeeService.getCommitteeById(productRequest.getCommitteeId());
        Product product = new Product(committee,
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getLimitPerOrder(),
                productRequest.getAvailableProducts());

        if (productRequest.getGroupId() != 0) {
            ProductGroup group = productGroupRepository.getById(productRequest.getGroupId());
            product.setProductGroup(group);
        }

        productRepository.saveAndFlush(product);
    }

    @Override
    public void addProductGroup(ProductGroupRequest productGroupRequest) {
        Committee committee = committeeService.getCommitteeById(productGroupRequest.getCommitteeId());
        ProductGroup group = new ProductGroup(productGroupRequest.getName(),
                productGroupRequest.getDescription(), productGroupRequest.getGroupLimit(), committee);

        productGroupRepository.saveAndFlush(group);
    }

    @Override
    public List<ProductGroup> getAllProductGroups() {
        return productGroupRepository.findAll();
    }

    @Override
    public void addProductToGroup(Product product, ProductGroup productGroup) {

        Product currentProduct = productRepository.getById(product.getId());

        currentProduct.setProductGroup(productGroup);
        productRepository.saveAndFlush(currentProduct);
    }

    @Override
    public void editProduct(ProductRequest productRequest) {
        if (productRequest.getId() != 0) {
            Committee committee = committeeService.getCommitteeById(productRequest.getCommitteeId());
            Product product = productRepository.getById(productRequest.getId());

            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setCommittee(committee);
            product.setPrice(productRequest.getPrice());
            product.setLimitPerOrder(productRequest.getLimitPerOrder());
            product.setAvailableProducts(productRequest.getAvailableProducts());

            product = productRepository.saveAndFlush(product);

            if (productRequest.getGroupId() != 0) {
                ProductGroup group = productGroupRepository.getById(productRequest.getGroupId());
                addProductToGroup(product, group);
            } else {
                product.setProductGroup(null);
                productRepository.saveAndFlush(product);
            }
        }
    }

    @Override
    public void deleteProduct(int productId) {
        List<Order> orders = orderService.getOrdersByProductId(productId);

        if (orders.size() > 0) {
            throw new ProductInUseException("Products are already ordered");
        } else {
            productRepository.deleteById(productId);
        }
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.getById(productId);
    }

    @Override
    public Product getProductByKey(String key) {
        return productRepository.findOneByKey(key).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Set<Product> getProductByCommittee(CommitteeEnum committeeEnum, int year) {
        Committee committee = committeeService.getCommittee(committeeEnum, year);

        return productRepository.findByCommittee(committee);
    }

    @Override
    public boolean isProductAvailable(Integer productId) {
        Product product = productRepository.getById(productId);
        List<Order> orders = orderService.getOrdersByProductId(productId);

        // Count the number of products with the given ID in each order.
        long numberOfProducts = orders.stream().flatMap(o -> o.getProducts().stream())
                .filter(p -> p.equals(product))
                .count();

        return numberOfProducts < product.getAvailableProducts()
                || product.getAvailableProducts() == 0;
    }

    @Override
    public ProductGroup getProductGroupById(Integer productGroupId) {
        return productGroupRepository.getById(productGroupId);
    }

    @Override
    public void editProductGroup(ProductGroupRequest productGroupRequest) {
        if (productGroupRequest.getId() != 0) {
            Committee committee = committeeService.getCommitteeById(productGroupRequest.getCommitteeId());
            ProductGroup productGroup = productGroupRepository.getById(productGroupRequest.getId());

            productGroup.setCommittee(committee);
            productGroup.setName(productGroupRequest.getName());
            productGroup.setDescription(productGroupRequest.getDescription());
            productGroup.setGroupLimit(productGroupRequest.getGroupLimit());

            productGroupRepository.saveAndFlush(productGroup);
        }
    }

    @Override
    public void deleteProductGroup(int productGroupId) {
        ProductGroup productGroup = productGroupRepository.getById(productGroupId);
        if (!productGroup.getProducts().isEmpty()) {
            throw new ProductGroupInUseException("Product group must be empty");
        } else {
            productGroupRepository.deleteById(productGroupId);
        }
    }
}
