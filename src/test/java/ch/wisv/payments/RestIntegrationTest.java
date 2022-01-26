package ch.wisv.payments;

import ch.wisv.payments.admin.committees.CommitteeRepository;
import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.CommitteeEnum;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.rest.MailServiceImpl;
import ch.wisv.payments.rest.MolliePaymentService;
import ch.wisv.payments.rest.repository.ProductGroupRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Year;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PaymentsApplicationTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class RestIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Value("${local.server.port}")
    int port;

    @Autowired
    protected CommitteeRepository committeeRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ProductGroupRepository productGroupRepository;

    @MockBean
    protected MolliePaymentService paymentService;

    @MockBean
    protected MailServiceImpl mailService;

    @Before
    public void setXAuthIntegrationTest() {
        RestAssured.port = port;
        RestAssured.config().redirect(RedirectConfig.redirectConfig().followRedirects(false));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @After
    public void tearDownRestIntegrationTest() {

    }

    protected Committee getOrPersistCommittee(CommitteeEnum committeeEnum) {
        return committeeRepository.findOneByNameAndYear(committeeEnum, Year.MIN_VALUE)
                .orElseGet(() -> committeeRepository.save(new Committee(committeeEnum, Year.MIN_VALUE)));
    }

    protected Committee getOrPersistW3Cie() {
        return getOrPersistCommittee(CommitteeEnum.W3CIE);
    }

    protected Product addProduct() {
        Committee committee = getOrPersistW3Cie();
        long count = productRepository.count();
        Product product = new Product(committee, "Product" + count, "Product" + count + " description", count, 0, 0);
        return productRepository.saveAndFlush(product);
    }
}
