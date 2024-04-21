package flightManagement.system.flight;

import flightManagement.system.exception.FlightException;
import flightManagement.system.flight.dto.FlightRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


/**
 * Service interface for managing Flight entities.
 */
public interface FlightService {

    /**
     * Retrieves a page of flights.
     *
     * @param pageable Pagination information.
     * @return A page of Flight entities.
     */
    Page<Flight> getAllFlights(Pageable pageable);

    /**
     * Adds a new flight based on the provided FlightRequest.
     *
     * @param flightRequest The FlightRequest containing flight details.
     * @return The newly created Flight entity.
     */
    Flight addNewFlight(FlightRequest flightRequest);

    /**
     * Counts the total number of flights.
     *
     * @return The total number of flights.
     */
    int countFlights();

    /**
     * Deletes a flight by its unique identifier.
     *
     * @param idFlight The unique identifier of the flight to delete.
     * @return A message indicating the success or failure of the deletion.
     */
    String deleteTheFlight(Long idFlight);

    /**
     * Edits a flight based on the provided FlightRequest and unique identifier.
     *
     * @param idFlight The unique identifier of the flight to edit.
     * @param flightRequest The FlightRequest containing updated flight details.
     * @return The edited Flight entity.
     * @throws FlightException If the flight with the specified ID does not exist.
     */
    Flight editFlight(Long idFlight, FlightRequest flightRequest);

    /**
     * Saves a flight entity.
     *
     * @param flight The flight entity to save.
     */
    void saveFlight(Flight flight);

    /**
     * Retrieves a flight by its unique identifier.
     *
     * @param idFlight The unique identifier of the flight.
     * @return An Optional containing the flight, or empty if not found.
     */
    Optional<Flight> findByIdFlight(Long idFlight);

    /**
     * Filters flights based on specified criteria.
     *
     * @param flightNumber The flight number to filter by.
     * @param departureCity The departure city to filter by.
     * @param arrivalCity The arrival city to filter by.
     * @param departureDate The departure date to filter by.
     * @param availableSeats The number of available seats to filter by.
     * @return A list of filtered Flight entities.
     * @throws IllegalArgumentException If the departure date has an invalid format.
     */
    List<Flight> filteringFlights(String flightNumber, String departureCity, String arrivalCity, String departureDate, Integer availableSeats);
}