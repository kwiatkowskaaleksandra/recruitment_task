package flightManagement.system.flight;

import flightManagement.system.exception.FlightException;
import flightManagement.system.flight.dto.FlightRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the FlightService interface.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    /**
     * Repository for accessing Flight entities
     */
    private final FlightRepository flightRepository;

    @Override
    public Page<Flight> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    @Transactional
    public Flight addNewFlight(FlightRequest flightRequest) {
        dataValidation(flightRequest);
        Time flightDuration = Time.valueOf(flightRequest.getFlightDuration().toLocalTime());
        FlightRoute flightRoute = createOrUpdateFlightRoute(flightRequest);

        Flight flight = new Flight();
        flight.setFlightNumber(flightRequest.getFlightNumber());
        flight.setAvailableSeats(flightRequest.getAvailableSeats());
        flight.setDepartureDate(flightRequest.getDepartureDate());
        flight.setFlightDuration(flightDuration);
        flight.setFlightRoute(flightRoute);
        saveFlight(flight);
        log.info("A new flight has been added.");
        return flight;
    }

    @Transactional
    public Flight editFlight(Long idFlight, FlightRequest flightRequest) {
        dataValidation(flightRequest);
        Optional<Flight> existingFlight = findByIdFlight(idFlight);
        if (existingFlight.isPresent()) {
            Time flightDuration = Time.valueOf(flightRequest.getFlightDuration().toLocalTime());
            FlightRoute flightRoute = createOrUpdateFlightRoute(flightRequest);

            Flight flightToEdit = existingFlight.get();
            flightToEdit.setFlightNumber(flightRequest.getFlightNumber());
            flightToEdit.setAvailableSeats(flightRequest.getAvailableSeats());
            flightToEdit.setDepartureDate(flightRequest.getDepartureDate());
            flightToEdit.setFlightDuration(flightDuration);
            flightToEdit.setFlightRoute(flightRoute);
            saveFlight(flightToEdit);
            log.info("The flight has been edited.");
            return flightToEdit;
        } else throw new FlightException("Lot o takim ID nie istnieje.");
    }

    @Override
    public void saveFlight(Flight flight) {
        flightRepository.save(flight);
    }

    @Override
    public Optional<Flight> findByIdFlight(Long idFlight) {
        return flightRepository.findByIdFlight(idFlight);
    }

    @Override
    public List<Flight> filteringFlights(String flightNumber, String departureCity, String arrivalCity, String departureDate, Integer availableSeats) {

        List<Flight> flightList = flightRepository.findAll();

        if (!flightNumber.equals("")) flightList = flightList.stream().filter(f -> f.getFlightNumber().equals(flightNumber)).collect(Collectors.toList());

        if (!departureCity.equals("")) flightList = flightList.stream().filter(f -> f.getFlightRoute().getDepartureCity().equals(departureCity)).collect(Collectors.toList());

        if (!arrivalCity.equals("")) flightList = flightList.stream().filter(f -> f.getFlightRoute().getArrivalCity().equals(arrivalCity)).collect(Collectors.toList());

        if (departureDate != null && !departureDate.isEmpty() && !"null".equals(departureDate)) {
            try {
                LocalDate departureDateOnly = LocalDate.parse(departureDate, DateTimeFormatter.ISO_DATE);
                flightList = flightList.stream()
                        .filter(f -> f.getDepartureDate().toLocalDate().isEqual(departureDateOnly))
                        .collect(Collectors.toList());
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid format for departure date: " + departureDate, e);
            }
        }


        if (availableSeats != null && availableSeats != 0) flightList = flightList.stream().filter(f -> f.getAvailableSeats() > 0).collect(Collectors.toList());

        return flightList;
    }

    @Override
    public int countFlights() {
        return flightRepository.findAll().size();
    }

    @Override
    public String deleteTheFlight(Long idFlight) {
        Optional<Flight> flight = findByIdFlight(idFlight);
        if (flight.isPresent()) {
            flightRepository.delete(flight.get());
            log.info("The flight was successfully removed.");
            return "Lot został usunięty.";
        }
        return "Nie ma lotu o takim ID.";
    }

    /**
     * Creates or updates a flight route based on the provided FlightRequest.
     *
     * @param flightRequest The FlightRequest containing flight route details.
     * @return The created or updated FlightRoute entity.
     */
    private FlightRoute createOrUpdateFlightRoute(FlightRequest flightRequest) {
        FlightRoute flightRoute = new FlightRoute();
        flightRoute.setDepartureCity(flightRequest.getDepartureCity());
        flightRoute.setArrivalCity(flightRequest.getArrivalCity());

        Set<IntermediateAirport> intermediateAirports = new HashSet<>();
        if (flightRequest.getIntermediateAirports() != null) {
            for (IntermediateAirport airport : flightRequest.getIntermediateAirports()) {
                if (airport != null && airport.getAirportName() != null && !airport.getAirportName().trim().isEmpty()) {
                    airport.setFlightRoute(flightRoute);
                    intermediateAirports.add(airport);
                }
            }
            if (!intermediateAirports.isEmpty()) {
                flightRoute.setIntermediateAirports(intermediateAirports);
            }
        }

        return flightRoute;
    }

    /**
     * Performs data validation on the provided FlightRequest.
     *
     * @param request The FlightRequest to validate.
     * @throws FlightException If any of the flight data is invalid.
     */
    private void dataValidation(FlightRequest request) {

        if (request.getFlightNumber().equals("") || request.getFlightNumber().length() != 5 || !request.getFlightNumber().startsWith("LO")) {
            log.error("Flight details are incorrectly filled in. The flight number details were entered incorrectly.");
            throw new FlightException("Dane dotyczące numeru lotu zostały źle uzupełnione. Numer lotu powinien zaczynać się od 'LO', a długość powinna być równa 5.");
        }

        if (request.getAvailableSeats() == 0) {
            log.error("Flight details are incorrectly filled in. The data regarding the number of available places was incorrectly completed.");
            throw new FlightException("Dane dotyczące ilości dostępnych miejsc zostały źle uzupełnione.");
        }

        if (request.getDepartureDate() == null || request.getDepartureDate().isBefore(LocalDateTime.now())) {
            log.error("Flight details are incorrectly filled in. The details regarding the departure date and time were incorrectly entered.");
            throw new FlightException("Dane dotyczące daty i czasu wylot zostały źle uzupełnione.");
        }

        if (request.getFlightDuration() == null) {
            log.error("Flight details are incorrectly filled in. Data regarding the duration of the flight were incorrectly completed.");
            throw new FlightException("Dane dotyczące długości trwania lotu zostały źle uzupełnione.");
        }

        if (request.getDepartureCity().equals("") || request.getArrivalCity().equals("")) {
            log.error("Flight details are incorrectly filled in. The data regarding the starting point and destination were incorrectly completed.");
            throw new FlightException("Dane dotyczące miejsca startu i miejsca docelowego zostały źle uzupełnione.");
        }

    }
}
