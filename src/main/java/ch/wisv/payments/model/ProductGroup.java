package ch.wisv.payments.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class ProductGroup {

    @Id
    @Getter
    @GeneratedValue
    private Integer id;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productGroup", cascade = CascadeType.PERSIST)
    private Set<Product> products;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String returnUrl;

    @Getter
    @Setter
    @ManyToOne
    private Committee committee;

    public ProductGroup(String name, String description, Committee committee) {
        this.name = name;
        this.description = description;
        this.committee = committee;
        this.products = new HashSet<>();
    }
}
