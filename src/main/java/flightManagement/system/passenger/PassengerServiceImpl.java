package flightManagement.system.passenger;

import flightManagement.system.exception.PassengerException;
import flightManagement.system.flight.Flight;
import flightManagement.system.flight.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of the PassengerService interface.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final FlightService flightService;

    @Override
    public Page<Passenger> getAllPassengerByIdFlight(Pageable pageable, Long idFlight) {
        return passengerRepository.findByFlight_IdFlight(idFlight, pageable);
    }

    @Override
    public Passenger addNewPassenger(Passenger passenger, Long idFlight) {
        dataValidation(passenger);
        Passenger newPassenger = new Passenger();
        Optional<Flight> flight = flightService.findByIdFlight(idFlight);

        if (flight.isPresent() && flight.get().getDepartureDate().isAfter(LocalDateTime.now()) && flight.get().getAvailableSeats() > 0) {
            newPassenger.setFirstname(passenger.getFirstname());
            newPassenger.setLastname(passenger.getLastname());
            newPassenger.setEmail(passenger.getEmail());
            newPassenger.setPhoneNumber(passenger.getPhoneNumber());
            newPassenger.setFlight(flight.get());

            flight.get().setAvailableSeats(flight.get().getAvailableSeats() - 1);
            flightService.saveFlight(flight.get());
            passengerRepository.save(newPassenger);
            log.info("A new passenger has been added.");
            return newPassenger;
        } else throw new  PassengerException("Brak wolnych miejsc na wybrany lot lub data wylotu już minęła.");
    }

    @Override
    public int countPassengerByIdFlight(Long idFlight) {
        return passengerRepository.findAllByFlight_IdFlight(idFlight).size();
    }

    @Override
    public String deletePassenger(Long idPassenger) {
        Optional<Passenger> passenger = passengerRepository.findByIdPassenger(idPassenger);
        if (passenger.isPresent()) {
            Flight flight = passenger.get().getFlight();
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
            flightService.saveFlight(flight);

            passengerRepository.delete(passenger.get());
            log.info("The passenger was successfully removed.");
            return "Pasażer został usunięty z listy pasażerów lotu.";
        }
        return "Pasażera nie ma na liście.";
    }

    @Override
    public Passenger editPassenger(Long idPassenger, Passenger passenger) {
        dataValidation(passenger);

        Optional<Passenger> existingPassenger = passengerRepository.findByIdPassenger(idPassenger);

        if (existingPassenger.isPresent()) {
            Passenger passengerToEdit = existingPassenger.get();
            passengerToEdit.setFirstname(passenger.getFirstname());
            passengerToEdit.setLastname(passenger.getLastname());
            passengerToEdit.setEmail(passenger.getEmail());
            passengerToEdit.setPhoneNumber(passenger.getPhoneNumber());

            passengerRepository.save(passengerToEdit);
            log.info("The passenger has been edited.");
            return passengerToEdit;
        } else throw new PassengerException("Nie ma takiego pasażera.");
    }

    /**
     * Validates the provided passenger data.
     *
     * @param passenger The passenger entity to validate.
     * @throws PassengerException If any of the passenger data is invalid.
     */
    private void dataValidation(Passenger passenger) {
        if (passenger.getFirstname().equals("") || passenger.getLastname().equals("")) {
            log.error("The passenger's name or surname was incorrect.");
            throw new PassengerException("Podano błędne imię lub nazwisko pasażera.");
        }

        if (passenger.getEmail().equals("") || !passenger.getEmail().matches(".*@.*")) {
            log.error("Invalid email address provided.");
            throw new PassengerException("Podano błędny adres email.");
        }

        if (passenger.getPhoneNumber().equals("") || passenger.getPhoneNumber().length() != 9) {
            log.error("The wrong phone number was entered.");
            throw new PassengerException("Podano błędny numer telefonu.");
        }
    }
}
