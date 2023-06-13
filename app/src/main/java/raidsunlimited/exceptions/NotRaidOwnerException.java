package raidsunlimited.exceptions;

/**
 * Exception to throw when user is not the Owner of the raid.
 */
public class NotRaidOwnerException extends RuntimeException {

    private static final long serialVersionUID = -2256091819882768344L;

    /**
     * Exception with no message or cause.
     */
    public NotRaidOwnerException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public NotRaidOwnerException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public NotRaidOwnerException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public NotRaidOwnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
