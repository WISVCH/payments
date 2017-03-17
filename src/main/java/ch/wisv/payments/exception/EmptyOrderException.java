package ch.wisv.payments.exception;

public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException(String s) {
        super(s);
    }
}

