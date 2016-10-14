package ch.wisv.payments.exception;

public class ProductInUseException extends RuntimeException {
    public ProductInUseException(String s) {
        super(s);
    }
}
