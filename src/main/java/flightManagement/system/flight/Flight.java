package flightManagement.system.flight;

import flightManagement.system.passenger.Passenger;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entity class representing a flight.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {

    /** The unique identifier of the flight. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFlight;

    /** The flight number. */
    private String flightNumber;

    /** The number of available seats on the flight. */
    private int availableSeats;

    /** The departure date and time of the flight. */
    private LocalDateTime departureDate;

    /** The duration of the flight. */
    private Time flightDuration;

    /** The route of the flight. */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flight_route", referencedColumnName = "idFlightRoute")
    private FlightRoute flightRoute;

    /** The set of passengers booked on the flight. */
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private Set<Passenger> passengers;
}