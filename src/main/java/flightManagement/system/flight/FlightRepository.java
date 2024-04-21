package flightManagement.system.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing Flight entities in the database.
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    /**
     * Retrieves a flight by its unique identifier.
     *
     * @param idFlight The unique identifier of the flight.
     * @return An Optional containing the flight, or empty if not found.
     */
    Optional<Flight> findByIdFlight(Long idFlight);
}