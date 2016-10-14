package ch.wisv.payments;

import ch.wisv.payments.admin.committees.CommitteeRepository;
import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.CommitteeEnum;
import ch.wisv.payments.rest.repository.ProductGroupRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Year;

import static io.restassured.RestAssured.config;
import static io.restassured.config.RedirectConfig.redirectConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class RestIntegrationTest {
    @Value("${local.server.port}")
    int port;

    @Autowired
    CommitteeRepository committeeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductGroupRepository productGroupRepository;


    private Committee committee;

    @Before
    public void initRestIntegrationTest() {
        RestAssured.port = port;
        config = config().redirect(redirectConfig().followRedirects(false));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        committee = new Committee(CommitteeEnum.W3CIE, Year.MIN_VALUE);
        committee = committeeRepository.save(committee);
    }

    @After
    public void tearDownRestIntegrationTest() {
        RestAssured.reset();
    }
}
