package flightManagement.system.flight;

import flightManagement.system.flight.dto.FlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling flight-related HTTP requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    /**
     * Retrieves a page of flights.
     *
     * @param page The page number.
     * @param size The size of each page.
     * @return ResponseEntity containing a page of Flight entities.
     */
    @GetMapping("/getAll")
    ResponseEntity<Page<Flight>> getAll(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(flightService.getAllFlights(pageable));
    }

    /**
     * Retrieves the total number of flights.
     *
     * @return ResponseEntity containing the total number of flights.
     */
    @GetMapping(value = "/countFlights")
    ResponseEntity<Integer> countFlights() {
        return ResponseEntity.ok(flightService.countFlights());
    }

    /**
     * Adds a new flight.
     *
     * @param flightRequest The FlightRequest containing flight details.
     * @return ResponseEntity containing the newly created Flight entity.
     */
    @PostMapping("/addNewFlight")
    ResponseEntity<Flight> addNewFlight(@RequestBody FlightRequest flightRequest) {
        return ResponseEntity.ok(flightService.addNewFlight(flightRequest));
    }

    /**
     * Deletes a flight by its ID.
     *
     * @param idFlight The ID of the flight to delete.
     * @return ResponseEntity containing a message indicating the success or failure of the deletion.
     */
    @DeleteMapping("/deleteTheFlight/{idFlight}")
    public ResponseEntity<String> deleteTheFlight(@PathVariable Long idFlight) {
        return ResponseEntity.ok(flightService.deleteTheFlight(idFlight));
    }

    /**
     * Edits a flight by its ID.
     *
     * @param flightRequest The FlightRequest containing updated flight details.
     * @param idFlight The ID of the flight to edit.
     * @return ResponseEntity containing the edited Flight entity.
     */
    @PutMapping("/editFlight/{idFlight}")
    public ResponseEntity<Flight> editFlight(@RequestBody FlightRequest flightRequest, @PathVariable Long idFlight) {
        return ResponseEntity.ok(flightService.editFlight(idFlight, flightRequest));
    }

    /**
     * Searches for flights based on specified criteria.
     *
     * @param flightNumber The flight number to search for.
     * @param departureCity The departure city to search for.
     * @param arrivalCity The arrival city to search for.
     * @param departureDate The departure date to search for.
     * @param availableSeats The number of available seats to search for.
     * @return ResponseEntity containing a list of filtered Flight entities.
     */
    @GetMapping("/searchFlights")
    public ResponseEntity<List<Flight>> searchFlights(@RequestParam(required = false) String flightNumber,
                                                      @RequestParam(required = false) String departureCity,
                                                      @RequestParam(required = false) String arrivalCity,
                                                      @RequestParam(required = false) String departureDate,
                                                      @RequestParam(required = false) Integer availableSeats) {
        return ResponseEntity.ok(flightService.filteringFlights(flightNumber, departureCity, arrivalCity, departureDate, availableSeats));
    }
}
