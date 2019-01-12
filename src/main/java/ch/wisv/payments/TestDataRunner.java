package ch.wisv.payments;

import ch.wisv.payments.admin.committees.CommitteeRepository;
import ch.wisv.payments.admin.products.ProductService;
import ch.wisv.payments.model.*;
import ch.wisv.payments.rest.repository.OrderRepository;
import ch.wisv.payments.rest.repository.ProductGroupRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void run(String... evt) throws Exception {
        Committee committee1 = committeeRepository.save(new Committee(CommitteeEnum.LUCIE, 2015));
        Committee committee2 = committeeRepository.save(new Committee(CommitteeEnum.AKCIE, 2014));
        Committee committee3 = committeeRepository.save(new Committee(CommitteeEnum.LANCIE, 2016));
        Committee committee4 = committeeRepository.save(new Committee(CommitteeEnum.CHIPCIE, 2015));
        Committee committee5 = committeeRepository.save(new Committee(CommitteeEnum.SYMPOSIUM, 2016));
        Committee committee6 = committeeRepository.save(new Committee(CommitteeEnum.BESTUUR, 2018));

        ProductGroup sympoGroup = productGroupRepository.save(
                new ProductGroup("Symposium 2016", "Group for all CH Symposium tickets", 200, committee5));

        ProductGroup akcieGroup = productGroupRepository.save(
                new ProductGroup("AkCie 2016", "Group for all AkCie activities", 0, committee2));

        ProductGroup bestuurGroup = productGroupRepository.save(
                new ProductGroup("Bestuur 2018", "Group for all Bestuur activities", 0, committee6));

        Product sympoStudent = productRepository.save(
                new Product(committee5, "Student Ticket", "Ticket for students", 10.00F, 0, 0));
        Product sympoBurger = productRepository.save(
                new Product(committee5, "Regular Ticket", "Ticket for non-students", 20.00F, 0, 0));
        Product lucie = productRepository.save(
                new Product(committee1, "lucieticket", "Ticket for non-students", 50.00F, 0, 0));

        Product matCH_ticket = new Product(committee6, "MatCH ticket", "Ticket for the MatCH activity.", 3.00f, 5, 5);
        matCH_ticket.setKey("48b068ab-f4c9-46f7-bcf0-15b2f60bd1b6");
        Product product = productRepository.save(matCH_ticket);

        productService.addProductToGroup(sympoStudent, sympoGroup);
        productService.addProductToGroup(sympoBurger, sympoGroup);

        Order order1 = orderRepository.save(
                new Order(Collections.singletonList(sympoStudent), "Alice Bobson", "alice@bobson.com"));
        Order order2 = orderRepository.save(
                new Order(Arrays.asList(sympoStudent, sympoStudent), "Bob Charleston", "bob@charleston.com"));
        Order order3 = orderRepository.save(
                new Order(Collections.singletonList(sympoBurger), "Charlie Dodson", "charlie@dodson.com"));
        Order order4 = orderRepository.save(
                new Order(Arrays.asList(sympoBurger, sympoStudent, lucie), "Dick Edison", "dick@edison.com"));
        Order order5 = orderRepository.save(
                new Order(Collections.singletonList(lucie), "Eddie F", "dick@edison.com"));
    }
}
