package flightManagement.system.flight;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Entity class representing a flight route.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flightRoutes")
public class FlightRoute {

    /** The unique identifier of the flight route. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFlightRoute;

    /** The city of departure for the route. */
    private String departureCity;

    /** The city of arrival for the route. */
    private String arrivalCity;

    /** Set of intermediate airports on the route. */
    @OneToMany(mappedBy = "flightRoute", cascade = CascadeType.ALL)
    private Set<IntermediateAirport> intermediateAirports;

    /** The flight associated with this route. */
    @OneToOne(mappedBy = "flightRoute")
    @JsonIgnore
    private Flight flight;
}