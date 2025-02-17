package com.example.transportCompany.service;

import com.example.transportCompany.dto.TransportDto;
import com.example.transportCompany.dto.TransportResponseDto;
import com.example.transportCompany.mapper.TransportMapper;
import com.example.transportCompany.model.*;
import com.example.transportCompany.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransportServiceImplTest {

    @Mock
    private TransportRepository transportRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransportStatusRepository transportStatusRepository;

    @Mock
    private TransportCompanyRepository companyRepository;

    @Mock
    private TransportMapper transportMapper;

    @InjectMocks
    private TransportServiceImpl transportServiceImpl;

    @Test
    void testCreateTransportSuccess() {
        TransportDto transportDto = new TransportDto(
                null,
                "Sofia",
                "Plovdiv",
                LocalDateTime.now(), // startDate
                LocalDateTime.now().plusHours(3), // endDate
                BigDecimal.valueOf(100.00), // price
                TransportType.CARGO, // transportType
                BigDecimal.valueOf(500.0), // cargoWeight
                null, // passengersCount
                1, // vehicleId
                1, // driverId
                1, // clientId
                1, // statusId
                1, // companyId
                true // paid
        );

        Vehicle mockVehicle = new Vehicle();
        mockVehicle.setId(1);

        VehicleType mockVehicleType = new VehicleType();
        mockVehicleType.setName("TRUCK");
        mockVehicle.setVehicleType(mockVehicleType);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(mockVehicle));

        Employee mockDriver = new Employee();
        mockDriver.setId(1);
        mockDriver.setName("John Doe");

        Qualification heavyCargoQualification = new Qualification();
        heavyCargoQualification.setName("HEAVY_CARGO");

        mockDriver.setQualifications(List.of(heavyCargoQualification));
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockDriver));

        Client mockClient = new Client();
        mockClient.setId(1);
        mockClient.setName("ACME Corp.");
        when(clientRepository.findById(1)).thenReturn(Optional.of(mockClient));

        TransportStatus mockStatus = new TransportStatus();
        mockStatus.setId(1);
        mockStatus.setName("In Progress");
        when(transportStatusRepository.findById(1)).thenReturn(Optional.of(mockStatus));

        TransportCompany mockCompany = new TransportCompany();
        mockCompany.setId(1);
        mockCompany.setName("Best Transports");
        mockCompany.setRevenue(BigDecimal.ZERO);
        when(companyRepository.findById(1)).thenReturn(Optional.of(mockCompany));

        Transport mockTransport = new Transport();
        when(transportMapper.toEntity(transportDto)).thenReturn(mockTransport);
        when(transportRepository.save(mockTransport)).thenReturn(mockTransport);

        TransportResponseDto mockResponseDto = new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "John Doe", 1, "ACME Corp.",
                1, "Best Transports", 1, "In Progress"
        );
        when(transportMapper.toResponseDto(mockTransport)).thenReturn(mockResponseDto);

        TransportResponseDto response = transportServiceImpl.createTransport(transportDto);

        verify(vehicleRepository).findById(1);
        verify(employeeRepository).findById(1);
        verify(clientRepository).findById(1);
        verify(transportStatusRepository).findById(1);
        verify(companyRepository).findById(1);
        verify(transportRepository).save(mockTransport);

        assertNotNull(response);
        assertEquals("Sofia", response.startLocation());
        assertEquals("Plovdiv", response.endLocation());
        assertEquals(1, response.vehicleId());
        assertEquals("TRUCK", mockVehicleType.getName());
    }

    @Test
    void testGetTransportById() {
        Transport mockTransport = new Transport();
        mockTransport.setId(1);
        mockTransport.setStartLocation("Sofia");
        mockTransport.setEndLocation("Plovdiv");

        when(transportRepository.findById(1)).thenReturn(Optional.of(mockTransport));
        when(transportMapper.toResponseDto(mockTransport)).thenReturn(new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "Driver Name", 1, "Client Name",
                1, "Company Name", 1, "Status"
        ));

        TransportResponseDto response = transportServiceImpl.getTransportById(1);

        assertNotNull(response);
        assertEquals("Sofia", response.startLocation());
        assertEquals("Plovdiv", response.endLocation());
    }

    @Test
    void testGetAllTransports() {
        Transport mockTransport1 = new Transport();
        mockTransport1.setId(1);
        mockTransport1.setStartLocation("Sofia");

        Transport mockTransport2 = new Transport();
        mockTransport2.setId(2);
        mockTransport2.setStartLocation("Varna");

        when(transportRepository.findAll()).thenReturn(List.of(mockTransport1, mockTransport2));
        when(transportMapper.toResponseDto(mockTransport1)).thenReturn(new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "Driver Name", 1, "Client Name",
                1, "Company Name", 1, "Status"
        ));
        when(transportMapper.toResponseDto(mockTransport2)).thenReturn(new TransportResponseDto(
                2, "Varna", "Burgas", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                BigDecimal.valueOf(150.00), TransportType.CARGO, BigDecimal.valueOf(700.0),
                null, true, 2, "456-DEF", 2, "Driver Name", 2, "Client Name",
                2, "Company Name", 2, "Status"
        ));

        List<TransportResponseDto> response = transportServiceImpl.getAllTransports();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Sofia", response.get(0).startLocation());
        assertEquals("Varna", response.get(1).startLocation());
    }

    @Test
    void testDeleteTransport() {
        Transport mockTransport = new Transport();
        mockTransport.setId(1);

        when(transportRepository.findById(1)).thenReturn(Optional.of(mockTransport));
        doNothing().when(transportRepository).delete(mockTransport);
        transportServiceImpl.deleteTransport(1);

        verify(transportRepository, times(1)).delete(mockTransport);
    }

    @Test
    void testGetTransportsByEndLocation() {
        Transport mockTransport = new Transport();
        mockTransport.setId(1);
        mockTransport.setEndLocation("Plovdiv");

        when(transportRepository.findByEndLocation("Plovdiv")).thenReturn(List.of(mockTransport));
        when(transportMapper.toResponseDto(mockTransport)).thenReturn(new TransportResponseDto(
                1, "Sofia", "Plovdiv", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                BigDecimal.valueOf(100.00), TransportType.CARGO, BigDecimal.valueOf(500.0),
                null, true, 1, "123-ABC", 1, "Driver Name", 1, "Client Name",
                1, "Company Name", 1, "Status"
        ));

        List<TransportResponseDto> response = transportServiceImpl.getTransportsByEndLocation("Plovdiv");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Plovdiv", response.get(0).endLocation());
    }
}