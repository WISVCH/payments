package ch.wisv.payments;

import ch.wisv.payments.rest.MailService;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class, SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@ActiveProfiles("test")
public class PaymentsApplicationTest {
    /**
     * Method main ...
     *
     * @param args of type String[]
     */
    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplicationTest.class, args);
    }

    /**
     * Method mailService ...
     *
     * @return MailService
     */
    @Bean
    @Primary
    public MailService mailService() {
        return Mockito.mock(MailService.class);
    }

    @Component
    public class TestRunner implements CommandLineRunner {

        /**
         * Callback used to run the bean.
         *
         * @param args incoming main method arguments
         *
         * @throws Exception on error
         */
        @Override
        public void run(String... args) throws Exception {

        }
    }

}
