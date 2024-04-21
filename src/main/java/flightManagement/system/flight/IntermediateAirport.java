package flightManagement.system.flight;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing an intermediate airport.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "intermediateAirports")
public class IntermediateAirport {

    /** The unique identifier of the intermediate airport. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIntermediateAirport;

    /** The name of the intermediate airport. */
    private String airportName;

    /** The flight route associated with this intermediate airport. */
    @ManyToOne
    @JoinColumn(name = "idFlightRoute")
    @JsonIgnore
    private FlightRoute flightRoute;
}