package raidsunlimited.exceptions;

/**
 * Exception to throw when a given raid ID is not found in the database.
 */
public class RaidEventNotFoundException extends RuntimeException {


    private static final long serialVersionUID = -6209896556717599530L;

    /**
     * Exception with no message or cause.
     */
    public RaidEventNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public RaidEventNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidEventNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidEventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
