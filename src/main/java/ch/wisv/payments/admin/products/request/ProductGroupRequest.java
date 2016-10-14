package ch.wisv.payments.admin.products.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ProductGroupRequest {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @NotEmpty
    private String name;

    @Getter
    @Setter
    @NotEmpty
    private String description;

    @Getter
    @Setter
    @NotNull
    private Integer committeeId;
}
