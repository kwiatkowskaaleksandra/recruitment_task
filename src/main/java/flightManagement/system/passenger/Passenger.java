package flightManagement.system.passenger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import flightManagement.system.flight.Flight;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing a passenger.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "passengers")
public class Passenger {

    /** The unique identifier of the passenger. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPassenger;

    /** The first name of the passenger. */
    private String firstname;

    /** The last name of the passenger. */
    private String lastname;

    /** The phone number of the passenger. */
    private String phoneNumber;

    /** The email address of the passenger. */
    private String email;

    /** The flight associated with this passenger. */
    @ManyToOne
    @JoinColumn(name = "idFlight")
    @JsonIgnore
    private Flight flight;
}