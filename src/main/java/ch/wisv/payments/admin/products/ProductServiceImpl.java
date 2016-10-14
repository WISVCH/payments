package ch.wisv.payments.admin.products;

import ch.wisv.payments.admin.committees.CommitteeRepository;
import ch.wisv.payments.admin.products.request.ProductGroupRequest;
import ch.wisv.payments.admin.products.request.ProductRequest;
import ch.wisv.payments.exception.ProductInUseException;
import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.model.ProductGroup;
import ch.wisv.payments.rest.OrderService;
import ch.wisv.payments.rest.repository.ProductGroupRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductGroupRepository productGroupRepository;
    private CommitteeRepository committeeRepository;
    private OrderService orderService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductGroupRepository productGroupRepository, CommitteeRepository committeeRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.productGroupRepository = productGroupRepository;
        this.committeeRepository = committeeRepository;
        this.orderService = orderService;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void addProduct(ProductRequest productRequest) {
        Committee committee = committeeRepository.findOne(productRequest.getCommitteeId());
        Product product = new Product(committee,
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getLimitPerOrder(),
                productRequest.getAvailableProducts());

        if (productRequest.getGroupId() != 0) {
            ProductGroup group = productGroupRepository.findOne(productRequest.getGroupId());
            product.setProductGroup(group);
        }

        productRepository.save(product);
    }

    @Override
    public void addProductGroup(ProductGroupRequest productGroupRequest) {
        Committee committee = committeeRepository.findOne(productGroupRequest.getCommitteeId());
        ProductGroup group = new ProductGroup(productGroupRequest.getName(),
                productGroupRequest.getDescription(), committee);

        productGroupRepository.save(group);
    }

    @Override
    public List<ProductGroup> getAllProductGroups() {
        return productGroupRepository.findAll();
    }

    @Override
    public void addProductToGroup(Product product, ProductGroup productGroup) {

        Product currentProduct = productRepository.findOne(product.getId());

        currentProduct.setProductGroup(productGroup);
        productRepository.save(currentProduct);
    }

    @Override
    public void editProduct(ProductRequest productRequest) {
        if (productRequest.getProductId() != 0) {
            Committee committee = committeeRepository.findOne(productRequest.getCommitteeId());
            Product product = productRepository.findOne(productRequest.getProductId());

            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setCommittee(committee);
            product.setPrice(productRequest.getPrice());

            product = productRepository.saveAndFlush(product);

            if (productRequest.getGroupId() != 0) {
                ProductGroup group = productGroupRepository.findOne(productRequest.getGroupId());
                addProductToGroup(product, group);
            } else {
                product.setProductGroup(null);
                productRepository.save(product);
            }
        }
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findOne(productId);
    }

    @Override
    public void deleteProduct(int productId) {
        List<Order> orders = orderService.getOrdersByProductId(productId);

        if (orders.size() > 0) {
            throw new ProductInUseException("Products are already ordered");
        } else {
            productRepository.delete(productId);
        }
    }
}
