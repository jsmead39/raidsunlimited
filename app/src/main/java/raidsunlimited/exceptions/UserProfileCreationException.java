package raidsunlimited.exceptions;

/**
 * Excpetion to throw when a user profile cannot be created.
 */
public class UserProfileCreationException extends RuntimeException {
    private static final long serialVersionUID = -1417772579391445084L;

    /**
     * Exception with no message or cause.
     */
    public UserProfileCreationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public UserProfileCreationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public UserProfileCreationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public UserProfileCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
