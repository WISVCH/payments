package ch.wisv.payments.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    @Getter
    int id;

    @Getter
    @Setter
    @ManyToOne
    Committee committee;

    @Getter
    @Setter
    String key;

    @Getter
    @Setter
    @ManyToOne
    private ProductGroup productGroup;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String description;

    @Getter
    @Setter
    float price;

    @Getter
    @Setter
    int limitPerOrder;
    @Getter
    @Setter
    int availableProducts;

    @Getter
    @Setter
    @Value("${a5l.paymentReturnUrl}")
    private String returnUrl;

    public Product(Committee committee, String name, String description, float price, int limitPerOrder, int availableProducts) {
        this.committee = committee;
        this.name = name;
        this.description = description;
        this.price = price;
        this.limitPerOrder = limitPerOrder;
        this.availableProducts = availableProducts;
        this.key = UUID.randomUUID().toString();
    }

    public Product(Committee committee, String name, String description, float price, int limitPerOrder, int availableProducts, String returnUrl) {
        this(committee, name, description, price, limitPerOrder, availableProducts);
        this.returnUrl = returnUrl;
    }
}
