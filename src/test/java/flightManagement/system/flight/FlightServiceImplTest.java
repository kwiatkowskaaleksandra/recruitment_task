package flightManagement.system.flight;

import flightManagement.system.exception.FlightException;
import flightManagement.system.flight.dto.FlightRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {

    @Mock FlightRepository flightRepository;
    @InjectMocks FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFlights() {
        Pageable pageable = Pageable.unpaged();
        Page<Flight> expectedPage = new PageImpl<>(Arrays.asList(new Flight(), new Flight()));
        when(flightRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Flight> result = flightService.getAllFlights(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(flightRepository).findAll(pageable);
    }

    @Test
    void testAddNewFlight_success() {
        FlightRequest request = new FlightRequest();
        request.setFlightNumber("LO123");
        request.setAvailableSeats(100);
        request.setDepartureDate(LocalDateTime.of(2023, 12, 15, 10, 0));
        request.setFlightDuration(java.sql.Time.valueOf("02:00:00"));
        request.setDepartureCity("Kielce");
        request.setArrivalCity("Opole");

        IntermediateAirport intermediateAirport = new IntermediateAirport();
        intermediateAirport.setAirportName("Chopin Warszawa");
        request.setIntermediateAirports(Set.of(intermediateAirport));

        when(flightRepository.save(ArgumentMatchers.<Flight>any())).thenAnswer(i -> i.getArguments()[0]);

        Flight result = flightService.addNewFlight(request);

        assertNotNull(result);
        assertEquals("LO123", result.getFlightNumber());

    }

    @Test
    void testAddNewFlight_InvalidFlightNumber() {
        FlightRequest request = new FlightRequest();
        request.setFlightNumber("123");
        request.setAvailableSeats(100);
        request.setDepartureDate(LocalDateTime.of(2023, 12, 15, 10, 0));
        request.setFlightDuration(java.sql.Time.valueOf("02:00:00"));
        request.setDepartureCity("Kielce");
        request.setArrivalCity("Opole");

        FlightException exception = assertThrows(FlightException.class, () -> flightService.addNewFlight(request));

        assertEquals("Dane dotyczące numeru lotu zostały źle uzupełnione. Numer lotu powinien zaczynać się od 'LO', a długość powinna być równa 5.", exception.getMessage());

    }

    @Test
    void testAddNewFlight_InvalidAvailableSeats() {
        FlightRequest request = new FlightRequest();
        request.setFlightNumber("LO123");
        request.setAvailableSeats(0);
        request.setDepartureDate(LocalDateTime.of(2023, 12, 15, 10, 0));
        request.setFlightDuration(java.sql.Time.valueOf("02:00:00"));
        request.setDepartureCity("Kielce");
        request.setArrivalCity("Opole");

        FlightException exception = assertThrows(FlightException.class, () -> flightService.addNewFlight(request));

        assertEquals("Dane dotyczące ilości dostępnych miejsc zostały źle uzupełnione.", exception.getMessage());

    }

    @Test
    void testAddNewFlight_InvalidDepartureDate() {
        FlightRequest request = new FlightRequest();
        request.setFlightNumber("LO123");
        request.setAvailableSeats(100);
        request.setDepartureDate(null);
        request.setFlightDuration(java.sql.Time.valueOf("02:00:00"));
        request.setDepartureCity("Kielce");
        request.setArrivalCity("Opole");

        FlightException exception = assertThrows(FlightException.class, () -> flightService.addNewFlight(request));

        assertEquals("Dane dotyczące daty i czasu wylot zostały źle uzupełnione.", exception.getMessage());

    }

    @Test
    void testAddNewFlight_InvalidFlightDuration() {
        FlightRequest request = new FlightRequest();
        request.setFlightNumber("LO123");
        request.setAvailableSeats(100);
        request.setDepartureDate(LocalDateTime.of(2023, 12, 15, 10, 0));
        request.setFlightDuration(null);
        request.setDepartureCity("Kielce");
        request.setArrivalCity("Opole");

        FlightException exception = assertThrows(FlightException.class, () -> flightService.addNewFlight(request));

        assertEquals("Dane dotyczące długości trwania lotu zostały źle uzupełnione.", exception.getMessage());
    }

    @Test
    void testAddNewFlight_InvalidDepartureCity() {
        FlightRequest request = new FlightRequest();
        request.setFlightNumber("LO123");
        request.setAvailableSeats(100);
        request.setDepartureDate(LocalDateTime.of(2023, 12, 15, 10, 0));
        request.setFlightDuration(java.sql.Time.valueOf("02:00:00"));
        request.setDepartureCity("");
        request.setArrivalCity("Opole");

        FlightException exception = assertThrows(FlightException.class, () -> flightService.addNewFlight(request));

        assertEquals("Dane dotyczące miejsca startu i miejsca docelowego zostały źle uzupełnione.", exception.getMessage());
    }


    @Test
    void testEditFlight() {
        Flight existingFlight = new Flight();
        existingFlight.setIdFlight(1L);
        existingFlight.setAvailableSeats(99);

        FlightRequest request = new FlightRequest();
        request.setFlightNumber("LO123");
        request.setAvailableSeats(100);
        request.setDepartureDate(LocalDateTime.of(2023, 12, 15, 10, 0));
        request.setFlightDuration(java.sql.Time.valueOf("02:00:00"));
        request.setDepartureCity("Warszawa");
        request.setArrivalCity("Opole");

        when(flightRepository.findByIdFlight(1L)).thenReturn(Optional.of(existingFlight));
        when(flightRepository.save(any(Flight.class))).thenAnswer(i -> i.getArguments()[0]);

        Flight result = flightService.editFlight(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getIdFlight());
        assertEquals(100, result.getAvailableSeats());
        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    void testEditFlight_error() {
        Flight existingFlight = new Flight();
        existingFlight.setIdFlight(1L);
        existingFlight.setAvailableSeats(99);

        FlightRequest request = new FlightRequest();
        request.setFlightNumber("LO123");
        request.setAvailableSeats(100);
        request.setDepartureDate(LocalDateTime.of(2023, 12, 15, 10, 0));
        request.setFlightDuration(java.sql.Time.valueOf("02:00:00"));
        request.setDepartureCity("Warszawa");
        request.setArrivalCity("Opole");

        when(flightRepository.findByIdFlight(1L)).thenReturn(Optional.empty());

        FlightException exception = assertThrows(FlightException.class, () -> flightService.editFlight(1L, request));

        assertEquals("Lot o takim ID nie istnieje.", exception.getMessage());
    }

    @Test
    void saveFlight() {
        Flight flight = new Flight();
        flight.setFlightNumber("LO123");
        flight.setAvailableSeats(100);
        flight.setDepartureDate(LocalDateTime.now());

        flightService.saveFlight(flight);

        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testFindByIdFlightExists() {
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setIdFlight(flightId);
        flight.setFlightNumber("LO123");
        when(flightRepository.findByIdFlight(flightId)).thenReturn(Optional.of(flight));

        Optional<Flight> foundFlight = flightService.findByIdFlight(flightId);

        assertTrue(foundFlight.isPresent());
        assertEquals(flightId, foundFlight.get().getIdFlight());
    }

    @Test
    void testFindByIdFlightDoesNotExist() {
        Long flightId = 99L;
        when(flightRepository.findByIdFlight(flightId)).thenReturn(Optional.empty());

        Optional<Flight> foundFlight = flightService.findByIdFlight(flightId);

        assertFalse(foundFlight.isPresent());
    }


    @Test
    void filteringFlights() {
        FlightRoute flightRoute1 = new FlightRoute();
        flightRoute1.setArrivalCity("Kielce");
        flightRoute1.setDepartureCity("Opole");
        Flight flight1 = new Flight();
        flight1.setFlightNumber("LO123");
        flight1.setAvailableSeats(150);
        flight1.setDepartureDate(LocalDate.of(2025, 10, 15).atStartOfDay());
        flight1.setFlightRoute(flightRoute1);

        FlightRoute flightRoute2 = new FlightRoute();
        flightRoute2.setArrivalCity("Kielce");
        flightRoute2.setDepartureCity("Opole");
        Flight flight2 = new Flight();
        flight2.setFlightNumber("LO456");
        flight2.setAvailableSeats(50);
        flight2.setDepartureDate(LocalDate.of(2025, 10, 20).atStartOfDay());
        flight2.setFlightRoute(flightRoute2);

        List<Flight> flights = Arrays.asList(flight1, flight2);
        when(flightRepository.findAll()).thenReturn(flights);

        List<Flight> result = flightService.filteringFlights("LO123", "Opole", "Kielce", "2025-10-15", 1);

        assertEquals(1, result.size());
        assertEquals("LO123", result.get(0).getFlightNumber());
    }

    @Test
    void filteringFlights_Error() {
        FlightRoute flightRoute1 = new FlightRoute();
        flightRoute1.setArrivalCity("Kielce");
        flightRoute1.setDepartureCity("Opole");
        Flight flight1 = new Flight();
        flight1.setFlightNumber("LO123");
        flight1.setAvailableSeats(150);
        flight1.setDepartureDate(LocalDate.of(2025, 10, 15).atStartOfDay());
        flight1.setFlightRoute(flightRoute1);

        FlightRoute flightRoute2 = new FlightRoute();
        flightRoute2.setArrivalCity("Kielce");
        flightRoute2.setDepartureCity("Opole");
        Flight flight2 = new Flight();
        flight2.setFlightNumber("LO456");
        flight2.setAvailableSeats(50);
        flight2.setDepartureDate(LocalDate.of(2025, 10, 20).atStartOfDay());
        flight2.setFlightRoute(flightRoute2);

        List<Flight> flights = Arrays.asList(flight1, flight2);
        when(flightRepository.findAll()).thenReturn(flights);

        assertThrows(IllegalArgumentException.class, () ->  flightService.filteringFlights("LO123", "Opole", "Kielce", "2025-10-44", 1));

    }

    @Test
    void countFlights() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());
        flights.add(new Flight());
        flights.add(new Flight());
        when(flightRepository.findAll()).thenReturn(flights);

        int result = flightService.countFlights();

        assertEquals(3, result);

        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void testDeleteTheFlight() {
        Flight flight = new Flight();
        flight.setIdFlight(1L);

        when(flightRepository.findByIdFlight(1L)).thenReturn(Optional.of(flight));
        doNothing().when(flightRepository).delete(flight);

        flightService.deleteTheFlight(1L);

        verify(flightRepository).delete(flight);
    }

}