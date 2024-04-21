package flightManagement.system.passenger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing Passenger entities in the database.
 */
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    /**
     * Retrieves a passenger by its unique identifier.
     *
     * @param idPassenger The unique identifier of the passenger.
     * @return An Optional containing the passenger, or empty if not found.
     */
    Optional<Passenger> findByIdPassenger(Long idPassenger);

    /**
     * Retrieves a page of passengers by the ID of the associated flight.
     *
     * @param flight_idFlight The ID of the associated flight.
     * @param pageable Pagination information.
     * @return A page of Passenger entities.
     */
    Page<Passenger> findByFlight_IdFlight(Long flight_idFlight, Pageable pageable);

    /**
     * Retrieves a list of passengers by the ID of the associated flight.
     *
     * @param flight_idFlight The ID of the associated flight.
     * @return A list of Passenger entities.
     */
    List<Passenger> findAllByFlight_IdFlight(Long flight_idFlight);
}
