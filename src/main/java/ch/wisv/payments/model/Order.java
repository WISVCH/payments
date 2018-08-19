package ch.wisv.payments.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @GeneratedValue
    @Id
    @Getter
    int id;

    @Getter
    @ManyToMany
    List<Product> products;

    @Getter
    LocalDateTime creationDate;

    @Getter
    @Setter
    LocalDateTime paidDate;

    @Getter
    @Setter
    OrderStatus status;

    @Getter
    @Setter
    MollieMethodEnum method;

    @Getter
    String publicReference;

    @Getter
    @Setter
    String providerReference;

    @Getter
    String name;

    @Getter
    String email;

    @Getter
    @Setter
    String paymentURL;

    @Getter
    @Setter
    String returnURL;

    @Getter
    @Setter
    boolean mailConfirmation;

    public Order(List<Product> products, String name, String email) {
        this.products = products;
        this.creationDate = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
        this.name = name;
        this.email = email;
        this.publicReference = UUID.randomUUID().toString();
        this.mailConfirmation = true;
    }

}
