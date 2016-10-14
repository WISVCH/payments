package ch.wisv.payments.exception;

public class ProductLimitExceededException extends RuntimeException {
    public ProductLimitExceededException(String s) {
        super(s);
    }
}
