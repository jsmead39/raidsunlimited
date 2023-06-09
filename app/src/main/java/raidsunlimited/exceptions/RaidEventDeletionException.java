package raidsunlimited.exceptions;

/**
 * Exception to throw when a raid fails to delete.
 */
public class RaidEventDeletionException extends RuntimeException {


    private static final long serialVersionUID = 4743586300377083856L;

    /**
     * Exception with no message or cause.
     */
    public RaidEventDeletionException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public RaidEventDeletionException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidEventDeletionException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidEventDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
