package flightManagement.system.passenger;

import flightManagement.system.exception.PassengerException;
import flightManagement.system.flight.Flight;
import flightManagement.system.flight.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passengerService = new PassengerServiceImpl(passengerRepository, flightService);
        Passenger passenger = new Passenger();
        passenger.setFirstname("John");
        passenger.setLastname("Doe");
        passenger.setEmail("john.doe@example.com");
        passenger.setPhoneNumber("123456789");

        Flight flight = new Flight();
        flight.setIdFlight(1L);
        flight.setDepartureDate(LocalDateTime.now().plusDays(1));
        flight.setAvailableSeats(10);
    }

    @Test
    public void testGetAllPassengerByIdFlight() {
        Pageable pageable = mock(Pageable.class);
        Long idFlight = 1L;
        Page<Passenger> expectedPage = mock(Page.class);

        when(passengerRepository.findByFlight_IdFlight(idFlight, pageable)).thenReturn(expectedPage);

        Page<Passenger> result = passengerService.getAllPassengerByIdFlight(pageable, idFlight);

        assertEquals(expectedPage, result);
        verify(passengerRepository).findByFlight_IdFlight(idFlight, pageable);
    }

    @Test
    public void testAddNewPassengerSuccessfully() {
        Passenger passenger = new Passenger();
        passenger.setFirstname("John");
        passenger.setLastname("Doe");
        passenger.setEmail("john.doe@example.com");
        passenger.setPhoneNumber("123456789");

        Long idFlight = 1L;
        Flight flight = new Flight();
        flight.setIdFlight(idFlight);
        flight.setDepartureDate(LocalDateTime.now().plusDays(1));
        flight.setAvailableSeats(1);

        when(flightService.findByIdFlight(idFlight)).thenReturn(Optional.of(flight));

        Passenger result = passengerService.addNewPassenger(passenger, idFlight);

        assertNotNull(result);
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("123456789", result.getPhoneNumber());
        verify(flightService).saveFlight(flight);
    }

    @Test
    public void testAddNewPassengerFailure_flightNotFound() {
        Passenger passenger = new Passenger();
        passenger.setFirstname("John");
        passenger.setLastname("Doe");
        passenger.setEmail("john.doe@example.com");
        passenger.setPhoneNumber("123456789");
        Long idFlight = 1L;

        when(flightService.findByIdFlight(idFlight)).thenReturn(Optional.empty());

        PassengerException exception = assertThrows(PassengerException.class, () -> passengerService.addNewPassenger(passenger, idFlight));

        assertEquals("Brak wolnych miejsc na wybrany lot lub data wylotu już minęła.", exception.getMessage());
    }

    @Test
    public void testAddNewPassengerFailure_IncorrectFirstname() {
        Passenger passenger = new Passenger();
        passenger.setFirstname("");
        Long idFlight = 1L;
        Flight flight = new Flight();
        flight.setIdFlight(idFlight);
        flight.setDepartureDate(LocalDateTime.now().minusDays(1));
        flight.setAvailableSeats(0);

        when(flightService.findByIdFlight(idFlight)).thenReturn(Optional.of(flight));

        PassengerException exception = assertThrows(PassengerException.class, () -> passengerService.addNewPassenger(passenger, idFlight));

        assertEquals("Podano błędne imię lub nazwisko pasażera.", exception.getMessage());
    }

    @Test
    public void testAddNewPassengerFailure_IncorrectEmail() {
        Passenger passenger = new Passenger();
        passenger.setFirstname("John");
        passenger.setLastname("Doe");
        passenger.setEmail("john.com");

        Long idFlight = 1L;
        Flight flight = new Flight();
        flight.setIdFlight(idFlight);
        flight.setDepartureDate(LocalDateTime.now().minusDays(1));
        flight.setAvailableSeats(0);

        when(flightService.findByIdFlight(idFlight)).thenReturn(Optional.of(flight));

        PassengerException exception = assertThrows(PassengerException.class, () -> passengerService.addNewPassenger(passenger, idFlight));

        assertEquals("Podano błędny adres email.", exception.getMessage());
    }

    @Test
    public void testAddNewPassengerFailure_IncorrectPhoneNumber() {
        Passenger passenger = new Passenger();
        passenger.setFirstname("John");
        passenger.setLastname("Doe");
        passenger.setEmail("john@example.com");
        passenger.setPhoneNumber("123");

        Long idFlight = 1L;
        Flight flight = new Flight();
        flight.setIdFlight(idFlight);
        flight.setDepartureDate(LocalDateTime.now().minusDays(1));
        flight.setAvailableSeats(0);

        when(flightService.findByIdFlight(idFlight)).thenReturn(Optional.of(flight));

        PassengerException exception = assertThrows(PassengerException.class, () -> passengerService.addNewPassenger(passenger, idFlight));

        assertEquals("Podano błędny numer telefonu.", exception.getMessage());
    }

    @Test
    public void testCountPassengerByIdFlight() {
        Long idFlight = 1L;
        List<Passenger> passengers = Arrays.asList(new Passenger(), new Passenger());

        when(passengerRepository.findAllByFlight_IdFlight(idFlight)).thenReturn(passengers);

        int count = passengerService.countPassengerByIdFlight(idFlight);

        assertEquals(2, count);
        verify(passengerRepository).findAllByFlight_IdFlight(idFlight);
    }

    @Test
    public void testDeletePassengerSuccessfully() {
        Long idPassenger = 1L;
        Passenger passenger = new Passenger();
        Flight flight = new Flight();
        flight.setAvailableSeats(5);

        passenger.setFlight(flight);

        when(passengerRepository.findByIdPassenger(idPassenger)).thenReturn(Optional.of(passenger));

        passengerService.deletePassenger(idPassenger);

        assertEquals(6, flight.getAvailableSeats());
        verify(flightService).saveFlight(flight);
        verify(passengerRepository).delete(passenger);
    }

    @Test
    public void testDeletePassengerFailureDueToNonExistentPassenger() {
        Long idPassenger = 1L;

        when(passengerRepository.findByIdPassenger(idPassenger)).thenReturn(Optional.empty());

        passengerService.deletePassenger(idPassenger);

        verify(passengerRepository, never()).delete((Passenger) null);
        verify(flightService, never()).saveFlight((Flight) null);
    }

    @Test
    public void testEditPassengerSuccess() {
        Long idPassenger = 1L;
        Passenger existingPassenger = new Passenger();
        existingPassenger.setIdPassenger(idPassenger);
        existingPassenger.setFirstname("OldName");
        existingPassenger.setLastname("OldLastName");

        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setFirstname("NewName");
        updatedPassenger.setLastname("NewLastName");
        updatedPassenger.setEmail("new.email@example.com");
        updatedPassenger.setPhoneNumber("123456789");

        when(passengerRepository.findByIdPassenger(idPassenger)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.save(isA(Passenger.class))).thenReturn(updatedPassenger);

        Passenger result = passengerService.editPassenger(idPassenger, updatedPassenger);

        assertEquals("NewName", result.getFirstname());
        assertEquals("NewLastName", result.getLastname());
        assertEquals("new.email@example.com", result.getEmail());
        assertEquals("123456789", result.getPhoneNumber());
    }

    @Test
    public void testEditPassengerFailureDueToNonExistentPassenger() {
        Long idPassenger = 1L;
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setFirstname("NewName");
        updatedPassenger.setLastname("NewLastName");
        updatedPassenger.setEmail("new.email@example.com");
        updatedPassenger.setPhoneNumber("123456789");

        when(passengerRepository.findByIdPassenger(idPassenger)).thenReturn(Optional.empty());

        PassengerException exception = assertThrows(PassengerException.class, () -> passengerService.editPassenger(idPassenger, updatedPassenger));

        assertEquals("Nie ma takiego pasażera.", exception.getMessage());
        verify(passengerRepository, never()).save(Mockito.<Passenger>any());
    }

}