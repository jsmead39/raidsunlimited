package raidsunlimited.exceptions;

/**
 * Exception to throw when a raid is already completed.
 */
public class RaidEventCompletionException extends RuntimeException {


    private static final long serialVersionUID = -3483371546222338318L;

    /**
     * Exception with no message or cause.
     */
    public RaidEventCompletionException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public RaidEventCompletionException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidEventCompletionException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidEventCompletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
