package flightManagement.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for handling exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles generic exceptions and returns a ResponseEntity with appropriate error details.
     *
     * @param ex The exception to handle.
     * @return ResponseEntity containing error details.
     */
    private ResponseEntity<?> handleException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Handles FlightException and returns a ResponseEntity with appropriate error details.
     *
     * @param ex The FlightException to handle.
     * @return ResponseEntity containing error details.
     */
    @ExceptionHandler({FlightException.class})
    public ResponseEntity<?> handleFlightException(FlightException ex) {
        return handleException(ex);
    }

    /**
     * Handles PassengerException and returns a ResponseEntity with appropriate error details.
     *
     * @param ex The PassengerException to handle.
     * @return ResponseEntity containing error details.
     */
    @ExceptionHandler({PassengerException.class})
    public ResponseEntity<?> handlePassengerException(PassengerException ex) {
        return handleException(ex);
    }
}
