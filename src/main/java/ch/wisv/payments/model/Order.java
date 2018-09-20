package ch.wisv.payments.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Data
public class Order {

    @GeneratedValue
    @Id
    @Setter(AccessLevel.NONE)
    int id;

    @ManyToMany
    @Setter(AccessLevel.NONE)
    List<Product> products;

    @Setter(AccessLevel.NONE)
    LocalDateTime creationDate;

    LocalDateTime paidDate;

    OrderStatus status;

    MollieMethodEnum method;

    @Setter(AccessLevel.NONE)
    String publicReference;

    String providerReference;

    @Setter(AccessLevel.NONE)
    String name;

    @Setter(AccessLevel.NONE)
    String email;

    String paymentURL;

    String returnURL;

    String webhookUrl;

    boolean mailConfirmation;

    public Order(List<Product> products, String name, String email) {
        this.products = products;
        this.creationDate = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
        this.name = name;
        this.email = email;
        this.publicReference = UUID.randomUUID().toString();
        this.method = MollieMethodEnum.IDEAL;
        this.mailConfirmation = true;
    }

}
