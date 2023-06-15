package raidsunlimited.exceptions;


/**
 * Exception to throw when an error occurs withdrawing for a Raid.
 */
public class RaidWithdrawException extends RuntimeException {


    private static final long serialVersionUID = -4841821854803251554L;

    /**
     * Exception with no message or cause.
     */
    public RaidWithdrawException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public RaidWithdrawException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidWithdrawException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public RaidWithdrawException(String message, Throwable cause) {
        super(message, cause);
    }
}
