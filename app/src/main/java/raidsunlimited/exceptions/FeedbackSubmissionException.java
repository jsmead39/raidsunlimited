package raidsunlimited.exceptions;

/**
 * Exception to throw when a user tried to provide duplicate feedback.
 */
public class FeedbackSubmissionException extends RuntimeException {


    private static final long serialVersionUID = -2936870981006554216L;

    /**
     * Exception with no message or cause.
     */
    public FeedbackSubmissionException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public FeedbackSubmissionException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public FeedbackSubmissionException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public FeedbackSubmissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
