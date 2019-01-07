package ch.wisv.payments.model;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
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

    @URL
    String webhookUrl;

    // Default is set to IDEAL, as old implementations do not include the method type in their request.
    String method = "IDEAL";

    boolean mailConfirmation = true;
}
