package flightManagement.system.exception;

/**
 * Custom exception class for passenger-related exceptions.
 */
public class PassengerException extends RuntimeException {

    /**
     * Constructs a new PassengerException with the specified detail message.
     *
     * @param message The detail message.
     */
    public PassengerException(String message) {
        super(message);
    }
}