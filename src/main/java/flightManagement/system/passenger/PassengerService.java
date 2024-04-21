package flightManagement.system.passenger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing Passenger entities.
 */
public interface PassengerService {

    /**
     * Retrieves a page of passengers by the ID of the associated flight.
     *
     * @param pageable Pagination information.
     * @param idFlight The ID of the associated flight.
     * @return A page of Passenger entities.
     */
    Page<Passenger> getAllPassengerByIdFlight(Pageable pageable, Long idFlight);

    /**
     * Adds a new passenger to the specified flight.
     *
     * @param passenger The Passenger entity to add.
     * @param idFlight The ID of the flight to add the passenger to.
     * @return The added Passenger entity.
     */
    Passenger addNewPassenger(Passenger passenger, Long idFlight);

    /**
     * Counts the number of passengers on the specified flight.
     *
     * @param idFlight The ID of the flight.
     * @return The number of passengers.
     */
    int countPassengerByIdFlight(Long idFlight);

    /**
     * Deletes a passenger by its ID.
     *
     * @param idPassenger The ID of the passenger to delete.
     * @return A message indicating the success or failure of the deletion.
     */
    String deletePassenger(Long idPassenger);

    /**
     * Edits a passenger by its ID.
     *
     * @param idPassenger The ID of the passenger to edit.
     * @param passenger The updated Passenger entity.
     * @return The edited Passenger entity.
     */
    Passenger editPassenger(Long idPassenger, Passenger passenger);
}