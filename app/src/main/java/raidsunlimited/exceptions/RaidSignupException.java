package raidsunlimited.exceptions;

public class RaidSignupException extends RuntimeException {

    private static final long serialVersionUID = 7638251841106988596L;

    /**
     * Exception with no message or cause.
     */
    public RaidSignupException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public RaidSignupException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidSignupException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidSignupException(String message, Throwable cause) {
        super(message, cause);
    }
}
