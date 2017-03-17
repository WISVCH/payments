package ch.wisv.payments.admin.products.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@NoArgsConstructor
public class ProductRequest {

    @Getter
    @Setter
    int id;
    @Setter
    @Getter
    int committeeId;
    @Setter
    @Getter
    @NotEmpty
    String name;
    @Setter
    @Getter
    @NotEmpty
    String description;
    @Getter
    @Setter
    int groupId;
    @Setter
    @Getter
    float price;
    @Getter
    @Setter
    int limitPerOrder = 0;
    @Getter
    @Setter
    int availableProducts = 0;
    @Getter
    @Setter
    String returnURL;
}
