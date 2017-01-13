package ch.wisv.payments.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty
    List<String> productKeys;

    @NotEmpty
    String name;
    @NotEmpty
    String email;

    @NotEmpty
    String returnUrl;
}
