package com.example.transportCompany.controller;

import com.example.transportCompany.dto.TransportDto;
import com.example.transportCompany.dto.TransportResponseDto;
import com.example.transportCompany.model.TransportType;
import com.example.transportCompany.service.TransportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransportControllerTest {

    @InjectMocks
    private TransportController transportController;

    @Mock
    private TransportService transportService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transportController).build();
    }

    @Test
    void testCreateTransport() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TransportDto transportDto = new TransportDto(
                null, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, 1, 1, 1, 1, 1, true
        );

        TransportResponseDto responseDto = new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "John Doe", 1, "Client Name",
                1, "Company Name", 1, "Status"
        );

        when(transportService.createTransport(any(TransportDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transportDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startLocation").value("Sofia"))
                .andExpect(jsonPath("$.endLocation").value("Plovdiv"));
    }

    @Test
    void testUpdateTransport() throws Exception {
        TransportDto transportDto = new TransportDto(
                null, "Sofia", "Burgas", LocalDateTime.now(), LocalDateTime.now().plusHours(4),
                BigDecimal.valueOf(200.00), TransportType.CARGO, BigDecimal.valueOf(700.0),
                null, 2, 3, 4, 5, 6, false
        );

        TransportResponseDto responseDto = new TransportResponseDto(
                1, "Sofia", "Burgas", LocalDateTime.now(), LocalDateTime.now().plusHours(4),
                BigDecimal.valueOf(200.00), TransportType.CARGO, BigDecimal.valueOf(700.0),
                null, false, 2, "456-DEF", 3, "Jane Doe", 4, "Client Name",
                5, "Company Name", 6, "Updated Status"
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        when(transportService.updateTransport(eq(1), any(TransportDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/transports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transportDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startLocation").value("Sofia"))
                .andExpect(jsonPath("$.endLocation").value("Burgas"));
    }

    @Test
    void testGetTransportById() throws Exception {
        TransportResponseDto responseDto = new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "John Doe", 1, "Client Name",
                1, "Company Name", 1, "Status"
        );

        when(transportService.getTransportById(1)).thenReturn(responseDto);

        mockMvc.perform(get("/api/transports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startLocation").value("Sofia"))
                .andExpect(jsonPath("$.endLocation").value("Plovdiv"));
    }

    @Test
    void testGetAllTransports() throws Exception {
        TransportResponseDto responseDto1 = new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "John Doe", 1, "Client Name",
                1, "Company Name", 1, "Status"
        );

        TransportResponseDto responseDto2 = new TransportResponseDto(
                2, "Varna", "Burgas", LocalDateTime.now(), LocalDateTime.now().plusHours(4),
                BigDecimal.valueOf(200.00), TransportType.CARGO, BigDecimal.valueOf(600.0),
                null, true, 2, "456-DEF", 2, "Jane Doe", 2, "Client Name 2",
                2, "Company Name 2", 2, "Status 2"
        );

        when(transportService.getAllTransports()).thenReturn(List.of(responseDto1, responseDto2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testDeleteTransport() throws Exception {
        doNothing().when(transportService).deleteTransport(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/transports/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetTransportsByEndLocation() throws Exception {
        TransportResponseDto responseDto = new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "John Doe", 1, "Client Name",
                1, "Company Name", 1, "Status"
        );

        when(transportService.getTransportsByEndLocation("Plovdiv")).thenReturn(List.of(responseDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transports/by-end-location")
                        .param("endLocation", "Plovdiv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].endLocation").value("Plovdiv"));
    }

    @Test
    void testGetPaidTransports() throws Exception {
        TransportResponseDto responseDto = new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "John Doe", 1, "Client Name",
                1, "Company Name", 1, "Status"
        );

        when(transportService.getPaidTransports()).thenReturn(List.of(responseDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transports/paid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].paid").value(true));
    }
}