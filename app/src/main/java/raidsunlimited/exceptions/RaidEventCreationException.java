package raidsunlimited.exceptions;

public class RaidEventCreationException extends RuntimeException {
    /**
     *
     * @param message indiciating that the RaidEvent could not be created.
     */
    public RaidEventCreationException(String message) {
        super(message);
    }
}
