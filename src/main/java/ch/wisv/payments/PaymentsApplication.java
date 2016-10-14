package ch.wisv.payments;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class PaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }


    /**
     * This function inserts some testdata into the database for development testing.
     */
    @Bean
    @Profile("dev")
    CommandLineRunner init(TestDataRunner testDataRunner) {

        return testDataRunner;

    }

}
