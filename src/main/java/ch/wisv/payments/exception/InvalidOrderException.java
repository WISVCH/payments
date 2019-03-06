package ch.wisv.payments.exception;

/**
 * InvalidOrderException.
 */
public class InvalidOrderException extends RuntimeException {

    /**
     * InvalidOrderException constructor.
     * @param s of type String
     */
    public InvalidOrderException(String s) {
        super(s);
    }
}
