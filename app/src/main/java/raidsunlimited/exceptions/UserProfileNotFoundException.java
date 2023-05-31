package raidsunlimited.exceptions;

/**
 * Exception to throw when a user profile cannot be found.
 */
public class UserProfileNotFoundException extends RuntimeException {


    private static final long serialVersionUID = -3878006512998035641L;

    /**
     * Exception with no message or cause.
     */
    public UserProfileNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public UserProfileNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public UserProfileNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public UserProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
