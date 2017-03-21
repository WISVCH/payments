package ch.wisv.payments.admin.products;

import ch.wisv.payments.admin.products.request.ProductGroupRequest;
import ch.wisv.payments.admin.products.request.ProductRequest;
import ch.wisv.payments.model.CommitteeEnum;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.model.ProductGroup;

import java.util.List;
import java.util.Set;

public interface ProductService {

    /*
    * Products
    */

    List<Product> getAllProducts();

    Product getProductById(Integer productId);

    Set<Product> getProductByCommittee(CommitteeEnum committeeEnum, int year);

    boolean isProductAvailable(Integer productId);

    void addProduct(ProductRequest productRequest);

    void editProduct(ProductRequest productRequest);

    void deleteProduct(int productRequest);

    /*
    * ProductGroups
    */

    List<ProductGroup> getAllProductGroups();

    void addProductGroup(ProductGroupRequest productGroupRequest);

    void addProductToGroup(Product product, ProductGroup productGroup);

    void deleteProductGroup(int productGroupId);

    ProductGroup getProductGroupById(Integer productGroupId);

    void editProductGroup(ProductGroupRequest productGroupRequest);
}
