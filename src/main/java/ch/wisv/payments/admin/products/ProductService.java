package ch.wisv.payments.admin.products;

import ch.wisv.payments.admin.products.request.ProductGroupRequest;
import ch.wisv.payments.admin.products.request.ProductRequest;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.model.ProductGroup;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    void addProduct(ProductRequest productRequest);

    void addProductGroup(ProductGroupRequest productGroupRequest);

    List<ProductGroup> getAllProductGroups();

    void addProductToGroup(Product product, ProductGroup productGroup);

    void editProduct(ProductRequest productRequest);

    Product getProductById(Integer productId);

    void deleteProduct(int productRequest);
}
