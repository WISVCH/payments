package ch.wisv.payments.admin.products;

import ch.wisv.payments.admin.committees.CommitteeService;
import ch.wisv.payments.admin.products.request.ProductGroupRequest;
import ch.wisv.payments.admin.products.request.ProductRequest;
import ch.wisv.payments.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private CommitteeService committeeService;

    @Autowired
    public ProductController(ProductService productService, CommitteeService committeeService) {
        this.productService = productService;
        this.committeeService = committeeService;
    }

    @GetMapping
    public String products(Model model) {
        model.addAttribute("committees", committeeService.getAllCommittees());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("productGroups", productService.getAllProductGroups());
        model.addAttribute("productGroup", new ProductGroupRequest());

        return "products";
    }

    @GetMapping(value = "/edit/{productId}")
    public String productEdit(@PathVariable Integer productId, Model model) {
        model.addAttribute("committees", committeeService.getAllCommittees());
        model.addAttribute("productGroups", productService.getAllProductGroups());

        Product product = productService.getProductById(productId);
        ProductRequest productRequest = new ProductRequest();

        productRequest.setProductId(product.getId());
        productRequest.setName(product.getName());
        productRequest.setDescription(product.getDescription());
        productRequest.setPrice(product.getPrice());
        productRequest.setCommitteeId(product.getCommittee().getId());
        productRequest.setAvailableProducts(product.getAvailableProducts());
        productRequest.setLimitPerOrder(product.getLimitPerOrder());
        productRequest.setReturnURL(product.getReturnUrl());

        if (product.getProductGroup() != null) {
            productRequest.setGroupId(product.getProductGroup().getId());
        }

        model.addAttribute("product", productRequest);

        return "edit";
    }

    @PostMapping(value = "/add")
    public String addProduct(@ModelAttribute @Validated ProductRequest productRequest) {
        productService.addProduct(productRequest);

        return "redirect:/products";
    }

    @PostMapping(value = "/edit")
    public String editProduct(@ModelAttribute @Validated ProductRequest productRequest, RedirectAttributes redirectAttributes) {
        productService.editProduct(productRequest);

        redirectAttributes.addFlashAttribute("message", productRequest.getName() + " successfully updated!");

        return "redirect:/products";
    }

    @PostMapping(value = "/delete/{productId}")
    public String deleteProduct(@PathVariable int productId, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(productId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/products";
    }

    @PostMapping(value = "/addGroup")
    public String addProductGroup(@ModelAttribute @Validated ProductGroupRequest productGroupRequest) {
        productService.addProductGroup(productGroupRequest);

        return "redirect:/products";
    }
}
