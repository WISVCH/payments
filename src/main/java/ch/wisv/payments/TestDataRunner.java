package ch.wisv.payments;

import ch.wisv.payments.admin.committees.CommitteeRepository;
import ch.wisv.payments.admin.products.ProductService;
import ch.wisv.payments.model.*;
import ch.wisv.payments.rest.repository.OrderRepository;
import ch.wisv.payments.rest.repository.ProductGroupRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
@Profile("dev")
public class TestDataRunner implements CommandLineRunner {
    private OrderRepository orderRepository;
    private ProductGroupRepository productGroupRepository;
    private ProductRepository productRepository;

    private CommitteeRepository committeeRepository;

    private ProductService productService;

    @Autowired
    public TestDataRunner(OrderRepository orderRepository, ProductGroupRepository productGroupRepository, ProductRepository productRepository, CommitteeRepository committeeRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productGroupRepository = productGroupRepository;
        this.productRepository = productRepository;
        this.committeeRepository = committeeRepository;
        this.productService = productService;
    }

    @Override
    public void run(String... evt) throws Exception {
        Committee committee1 = committeeRepository.save(new Committee(CommitteeEnum.LUCIE, 2015));
        Committee committee2 = committeeRepository.save(new Committee(CommitteeEnum.AKCIE, 2014));
        Committee committee3 = committeeRepository.save(new Committee(CommitteeEnum.LANCIE, 2016));
        Committee committee4 = committeeRepository.save(new Committee(CommitteeEnum.CHIPCIE, 2015));
        Committee committee5 = committeeRepository.save(new Committee(CommitteeEnum.SYMPOSIUM, 2016));

        ProductGroup sympoGroup = productGroupRepository.save(
                new ProductGroup("Symposium 2016", "Group for all CH Symposium tickets", committee5));

        ProductGroup akcieGroup = productGroupRepository.save(
                new ProductGroup("AkCie 2016", "Group for all AkCie activities", committee2));

        Product sympoStudent = productRepository.save(
                new Product(committee5, "Student Ticket", "Ticket for students", 10.00F, 0, 0));
        Product sympoBurger = productRepository.save(
                new Product(committee5, "Regular Ticket", "Ticket for non-students", 20.00F, 0, 0));

        productService.addProductToGroup(sympoStudent, sympoGroup);
        productService.addProductToGroup(sympoBurger, sympoGroup);

        Order order1 = orderRepository.save(
                new Order(Collections.singletonList(sympoStudent), "Alice Bobson", "alice@bobson.com"));
        Order order2 = orderRepository.save(
                new Order(Arrays.asList(sympoStudent, sympoStudent), "Bob Charleston", "bob@charleston.com"));
        Order order3 = orderRepository.save(
                new Order(Collections.singletonList(sympoBurger), "Charlie Dodson", "charlie@dodson.com"));
        Order order4 = orderRepository.save(
                new Order(Arrays.asList(sympoBurger, sympoStudent), "Dick Edison", "dick@edison.com"));


    }
}
