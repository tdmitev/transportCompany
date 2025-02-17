package com.example.transportCompany.repository;

import com.example.transportCompany.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
class TransportRepositoryTest {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TransportStatusRepository statusRepository;

    @Autowired
    private TransportCompanyRepository companyRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    private TransportCompany company;
    private Vehicle vehicle;
    private Client client;
    private Employee driver;
    private TransportStatus status;
    private Transport transport;

    @BeforeEach
    void setUp() {

        company = new TransportCompany();
        company.setName("Test Company");
        company = companyRepository.save(company);

        VehicleType vehicleType = new VehicleType();
        vehicleType.setName("Truck");
        vehicleType = vehicleTypeRepository.save(vehicleType);

        vehicle = new Vehicle();
        vehicle.setLicensePlate("123-ABC");
        vehicle.setFuelConsumption(BigDecimal.valueOf(8.5));
        vehicle.setCompany(company);
        vehicle.setVehicleType(vehicleType);
        vehicle = vehicleRepository.save(vehicle);

        client = new Client();
        client.setName("Test Client");
        client.setEmail("client@example.com");
        client.setPhone("123456789");
        client = clientRepository.save(client);

        driver = new Employee();
        driver.setName("John Doe");
        driver.setPosition("Driver");
        driver.setSalary(BigDecimal.valueOf(1500.00));
        driver.setCompany(company);
        driver = employeeRepository.save(driver);

        status = new TransportStatus();
        status.setName("In Progress");
        status = statusRepository.save(status);

        transport = new Transport();
        transport.setStartLocation("Sofia");
        transport.setEndLocation("Plovdiv");
        transport.setStartDate(LocalDateTime.now());
        transport.setEndDate(LocalDateTime.now().plusHours(3));
        transport.setPrice(BigDecimal.valueOf(100.00));
        transport.setPaid(true);
        transport.setVehicle(vehicle);
        transport.setClient(client);
        transport.setDriver(driver);
        transport.setStatus(status);
        transport.setCompany(company);
        transport.setTransportType(TransportType.CARGO);

        transport = transportRepository.save(transport);
    }

    @Test
    void testSaveAndRetrieveTransport() {
        Transport newTransport = new Transport();
        newTransport.setStartLocation("Varna");
        newTransport.setEndLocation("Burgas");
        newTransport.setStartDate(LocalDateTime.now());
        newTransport.setEndDate(LocalDateTime.now().plusHours(4));
        newTransport.setPrice(BigDecimal.valueOf(200.00));
        newTransport.setPaid(false);
        newTransport.setVehicle(vehicle);
        newTransport.setClient(client);
        newTransport.setDriver(driver);
        newTransport.setStatus(status);
        newTransport.setCompany(company);
        newTransport.setTransportType(TransportType.CARGO);

        newTransport = transportRepository.save(newTransport);

        Optional<Transport> retrievedTransport = transportRepository.findById(newTransport.getId());

        assertTrue(retrievedTransport.isPresent());
        assertEquals("Varna", retrievedTransport.get().getStartLocation());
        assertEquals("Burgas", retrievedTransport.get().getEndLocation());
        assertEquals(BigDecimal.valueOf(200.00), retrievedTransport.get().getPrice());
        assertFalse(retrievedTransport.get().isPaid());
    }

    @Test
    void testFindByEndLocation() {
        List<Transport> result = transportRepository.findByEndLocation("Plovdiv");
        assertEquals(1, result.size());
        assertEquals("Plovdiv", result.get(0).getEndLocation());
    }

    @Test
    void testFindByPaidTrue() {
        List<Transport> result = transportRepository.findByPaidTrue();
        assertEquals(1, result.size());
        assertTrue(result.get(0).isPaid());
    }

    @Test
    void testFindByStartDateBetween() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(4);

        List<Transport> result = transportRepository.findByStartDateBetween(start, end);
        assertEquals(1, result.size());
        assertEquals("Sofia", result.get(0).getStartLocation());
    }

    @Test
    void testCountAllTransports() {
        Long count = transportRepository.countAllTransports();
        assertEquals(1, count);
    }

    @Test
    void testSumAllTransportPrices() {
        BigDecimal total = transportRepository.sumAllTransportPrices();
        assertEquals(BigDecimal.valueOf(100.0).setScale(2), total.setScale(2));
    }
}