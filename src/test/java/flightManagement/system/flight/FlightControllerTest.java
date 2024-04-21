package flightManagement.system.flight;

import com.fasterxml.jackson.databind.ObjectMapper;
import flightManagement.system.flight.dto.FlightRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class FlightControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FlightService flightService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flightController).addFilters(new CharacterEncodingFilter("UTF-8", true)).build();
    }

    @Test
    public void testGetAllFlights() throws Exception {
        Page<Flight> page = new PageImpl<>(Arrays.asList(new Flight(), new Flight()));
        when(flightService.getAllFlights(PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/api/flights/getAll?page=0&size=10"))
                .andExpect(status().isOk());
    }


    @Test
    void countFlights() throws Exception {
        when(flightService.countFlights()).thenReturn(10);

        mockMvc.perform(get("/api/flights/countFlights"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    public void testAddNewFlight() throws Exception {
        FlightRequest flightRequest = new FlightRequest();
        Flight flight = new Flight();

        when(flightService.addNewFlight(flightRequest)).thenReturn(flight);

        mockMvc.perform(post("/api/flights/addNewFlight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightRequest)))
                .andExpect(status().isOk());
    }


    @Test
    public void testDeleteTheFlight() throws Exception {
        Long idFlight = 1L;

        when(flightService.deleteTheFlight(idFlight)).thenReturn("Nie ma lotu o takim ID");

        mockMvc.perform(delete("/api/flights/deleteTheFlight/{idFlight}", idFlight))
                .andExpect(status().isOk());
    }


    @Test
    public void testEditFlight() throws Exception {
        Long idFlight = 1L;
        FlightRequest flightRequest = new FlightRequest();
        Flight editedFlight = new Flight();

        when(flightService.editFlight(idFlight, flightRequest)).thenReturn(editedFlight);

        mockMvc.perform(put("/api/flights/editFlight/{idFlight}", idFlight)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightRequest)))
                .andExpect(status().isOk());
    }


    @Test
    public void testSearchFlights() throws Exception {
        List<Flight> flights = Arrays.asList(new Flight(), new Flight());
        String flightNumber = "12345";
        String departureCity = "CityA";
        String arrivalCity = "CityB";
        String departureDate = "2023-10-01";
        Integer availableSeats = 100;

        when(flightService.filteringFlights(flightNumber, departureCity, arrivalCity, departureDate, availableSeats))
                .thenReturn(flights);

        mockMvc.perform(get("/api/flights/searchFlights")
                        .param("flightNumber", flightNumber)
                        .param("departureCity", departureCity)
                        .param("arrivalCity", arrivalCity)
                        .param("departureDate", departureDate)
                        .param("availableSeats", availableSeats.toString()))
                .andExpect(status().isOk());
    }

}