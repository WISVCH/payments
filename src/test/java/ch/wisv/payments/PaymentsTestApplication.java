package ch.wisv.payments;

import ch.wisv.payments.rest.MailServiceImpl;
import ch.wisv.payments.rest.MolliePaymentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@ActiveProfiles("test")
public class PaymentsTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentsTestApplication.class, args);
    }

    @MockBean
    MolliePaymentService paymentService;

    @MockBean
    MailServiceImpl mailService;
}
