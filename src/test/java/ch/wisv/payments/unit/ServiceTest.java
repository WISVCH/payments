package ch.wisv.payments.unit;

import ch.wisv.payments.PaymentsTestApplication;
import ch.wisv.payments.admin.committees.CommitteeRepository;
import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.CommitteeEnum;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.rest.repository.ProductRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentsTestApplication.class)
@ActiveProfiles("test")
@DataJpaTest
public abstract class ServiceTest {

    @Autowired
    protected CommitteeRepository committeeRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected TestEntityManager testEntityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        testEntityManager.clear();
    }

    Committee persistAndGetCommittee() {
        long count = committeeRepository.count();
        return testEntityManager.persist(new Committee(CommitteeEnum.W3CIE, (int) (1900 + count)));
    }

    Product persistAndGetProduct() {
        return persistAndGetProduct(persistAndGetCommittee());
    }

    Product persistAndGetProduct(Committee committee) {
        long count = productRepository.count();
        return testEntityManager.persist(new Product(committee, "product" + count, "description" + count, count, 0, 0));
    }

}
