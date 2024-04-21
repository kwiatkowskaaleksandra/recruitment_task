package flightManagement.system.passenger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PassengerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerController passengerController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(passengerController).addFilters(new CharacterEncodingFilter("UTF-8", true)).build();
    }

    @Test
    void testGetAllByIdFlight() throws Exception {
        Page<Passenger> page = new PageImpl<>(List.of(new Passenger()));
        when(passengerService.getAllPassengerByIdFlight(any(Pageable.class), anyLong())).thenReturn(page);

        mockMvc.perform(get("/api/passengers/getAllByIdFlight")
                        .param("page", "0")
                        .param("size", "10")
                        .param("idFlight", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(passengerService).getAllPassengerByIdFlight(any(Pageable.class), eq(1L));
    }

    @Test
    void testCountPassengersByIdFlight() throws Exception {
        when(passengerService.countPassengerByIdFlight(1L)).thenReturn(10);

        mockMvc.perform(get("/api/passengers/countPassengersByIdFlight/{idFlight}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(passengerService).countPassengerByIdFlight(1L);
    }

    @Test
    void testDeleteThePassenger() throws Exception {
        when(passengerService.deletePassenger(1L)).thenReturn("Nie ma lotu o takim ID.");

        mockMvc.perform(delete("/api/passengers/deleteThePassenger/{idPassenger}", 1))
                .andExpect(status().isOk());

        verify(passengerService).deletePassenger(1L);
    }

    @Test
    void testEditPassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setIdPassenger(1L);
        when(passengerService.editPassenger(eq(1L), any(Passenger.class))).thenReturn(passenger);

        mockMvc.perform(put("/api/passengers/editPassenger/{idPassenger}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"John\",\"lastname\":\"Doe\", \"phoneNumber\":\"123456789\", \"email\":\"john@example.pl\" }"))
                .andExpect(status().isOk());

        verify(passengerService).editPassenger(eq(1L), any(Passenger.class));
    }

    @Test
    void testAddNewPassenger() throws Exception {
        Passenger passenger = new Passenger();
        when(passengerService.addNewPassenger(any(Passenger.class), eq(1L))).thenReturn(passenger);

        mockMvc.perform(post("/api/passengers/addNewPassenger/{idFlight}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\"}"))
                .andExpect(status().isOk());

        verify(passengerService).addNewPassenger(any(Passenger.class), eq(1L));
    }
}