package ch.wisv.payments.exception;

public class ProductGroupInUseException extends RuntimeException {
    public ProductGroupInUseException(String s) {
        super(s);
    }
}
