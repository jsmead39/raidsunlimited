package raidsunlimited.exceptions;

/**
 * Exception to throw when a user tried to provide duplicate feedback.
 */
public class DuplicateFeedbackException extends RuntimeException {


    private static final long serialVersionUID = -2936870981006554216L;

    /**
     * Exception with no message or cause.
     */
    public DuplicateFeedbackException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public DuplicateFeedbackException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateFeedbackException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateFeedbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
