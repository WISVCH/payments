package ch.wisv.payments.rest;

import ch.wisv.payments.admin.products.ProductService;
import ch.wisv.payments.model.CommitteeEnum;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.model.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/products")
public class ProductRestController {

    private ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("{committeeName}/{year}")
    Set<ProductResponse> getCommitteeProducts(@PathVariable CommitteeEnum committeeName, @PathVariable int year) {

        Set<Product> productByCommittee = productService.getProductByCommittee(committeeName, year);
        return productByCommittee.stream().map(p -> new ProductResponse(p, productService.isProductAvailable(p.getId())))
                .collect(Collectors.toSet());
    }
}
