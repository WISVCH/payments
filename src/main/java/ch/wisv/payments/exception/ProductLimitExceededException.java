package ch.wisv.payments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ProductLimitExceededException extends RuntimeException {
    public ProductLimitExceededException(String s) {
        super(s);
    }
}
