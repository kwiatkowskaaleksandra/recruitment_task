package flightManagement.system.exception;

/**
 * Custom exception class for flight-related exceptions.
 */
public class FlightException extends RuntimeException {

    /**
     * Constructs a new FlightException with the specified detail message.
     *
     * @param message The detail message.
     */
    public FlightException(String message) {
        super(message);
    }
}