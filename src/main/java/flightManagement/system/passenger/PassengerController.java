package flightManagement.system.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling passenger-related HTTP requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    /**
     * Retrieves a page of passengers by the ID of the associated flight.
     *
     * @param page The page number.
     * @param size The size of each page.
     * @param idFlight The ID of the associated flight.
     * @return ResponseEntity containing a page of Passenger entities.
     */
    @GetMapping("/getAllByIdFlight")
    public ResponseEntity<Page<Passenger>> getAllByIdFlight(@RequestParam int page, @RequestParam int size, @RequestParam Long idFlight) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(passengerService.getAllPassengerByIdFlight(pageable, idFlight));
    }

    /**
     * Retrieves the number of passengers on the specified flight.
     *
     * @param idFlight The ID of the flight.
     * @return ResponseEntity containing the number of passengers.
     */
    @GetMapping("/countPassengersByIdFlight/{idFlight}")
    public ResponseEntity<Integer> countPassengersByIdFlight(@PathVariable Long idFlight) {
        return ResponseEntity.ok(passengerService.countPassengerByIdFlight(idFlight));
    }

    /**
     * Deletes a passenger by its ID.
     *
     * @param idPassenger The ID of the passenger to delete.
     * @return ResponseEntity containing a message indicating the success or failure of the deletion.
     */
    @DeleteMapping("/deleteThePassenger/{idPassenger}")
    public ResponseEntity<String> deleteThePassenger(@PathVariable Long idPassenger) {
        return ResponseEntity.ok(passengerService.deletePassenger(idPassenger));
    }

    /**
     * Edits a passenger by its ID.
     *
     * @param passenger The updated Passenger entity.
     * @param idPassenger The ID of the passenger to edit.
     * @return ResponseEntity containing the edited Passenger entity.
     */
    @PutMapping("/editPassenger/{idPassenger}")
    public ResponseEntity<Passenger> editPassenger(@RequestBody Passenger passenger, @PathVariable Long idPassenger) {
        return ResponseEntity.ok(passengerService.editPassenger(idPassenger, passenger));
    }

    /**
     * Adds a new passenger to the specified flight.
     *
     * @param passenger The Passenger entity to add.
     * @param idFlight The ID of the flight to add the passenger to.
     * @return ResponseEntity containing the added Passenger entity.
     */
    @PostMapping("/addNewPassenger/{idFlight}")
    public ResponseEntity<Passenger> addNewPassenger(@RequestBody Passenger passenger, @PathVariable Long idFlight) {
        return ResponseEntity.ok(passengerService.addNewPassenger(passenger, idFlight));
    }
}