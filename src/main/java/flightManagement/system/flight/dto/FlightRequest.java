package flightManagement.system.flight.dto;

import flightManagement.system.flight.IntermediateAirport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A data class representing a flight request.
 */
@Getter
@Setter
@ToString
public class FlightRequest {

    /** The flight number. */
    private String flightNumber;

    /** The number of available seats. */
    private int availableSeats;

    /** The departure date and time. */
    private LocalDateTime departureDate;

    /** The duration of the flight. */
    private Time flightDuration;

    /** The city of departure. */
    private String departureCity;

    /** The city of arrival. */
    private String arrivalCity;

    /** Set of intermediate airports for the flight. */
    private Set<IntermediateAirport> intermediateAirports;
}