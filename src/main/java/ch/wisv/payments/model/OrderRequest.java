package ch.wisv.payments.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty
    List<String> productKeys;

    @NotEmpty
    String name;
    @NotEmpty
    String email;

    @URL
    String returnUrl;

    boolean mailConfirmation = true;
}
