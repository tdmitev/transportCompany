package com.example.transportCompany.service;

import com.example.transportCompany.dto.DriverTransportSummaryDto;
import com.example.transportCompany.dto.TransportDto;
import com.example.transportCompany.dto.TransportResponseDto;
import com.example.transportCompany.exception.ResourceNotFoundException;
import com.example.transportCompany.mapper.TransportMapper;
import com.example.transportCompany.model.*;
import com.example.transportCompany.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransportServiceImpl implements TransportService {

    private final TransportRepository transportRepository;
    private final TransportMapper transportMapper;
    private final VehicleRepository vehicleRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final TransportStatusRepository transportStatusRepository;
    private final TransportCompanyRepository companyRepository;
    private static final String FILE_PATH = "data/transports.json";

    public TransportServiceImpl(TransportRepository transportRepository,
                                TransportMapper transportMapper,
                                VehicleRepository vehicleRepository,
                                EmployeeRepository employeeRepository,
                                ClientRepository clientRepository,
                                TransportStatusRepository transportStatusRepository,
                                TransportCompanyRepository companyRepository) {
        this.transportRepository = transportRepository;
        this.transportMapper = transportMapper;
        this.vehicleRepository = vehicleRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.transportStatusRepository = transportStatusRepository;
        this.companyRepository = companyRepository;
    }
    @Override
    public TransportResponseDto createTransport(TransportDto dto) {
        Transport transport = transportMapper.toEntity(dto);

        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + dto.vehicleId()));
        transport.setVehicle(vehicle);

        Employee driver = employeeRepository.findById(dto.driverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id " + dto.driverId()));
        transport.setDriver(driver);

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + dto.clientId()));
        transport.setClient(client);

        TransportStatus status = transportStatusRepository.findById(dto.statusId())
                .orElseThrow(() -> new ResourceNotFoundException("TransportStatus not found with id " + dto.statusId()));
        transport.setStatus(status);

        TransportCompany company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + dto.companyId()));
        transport.setCompany(company);

        validateTransportType(dto);
        checkVehicleType(dto.transportType(), dto.passengersCount(), vehicle);
        checkDriverQualifications(dto.transportType(), dto.cargoWeight(), dto.passengersCount(), driver);

        if (dto.paid()) {
            company.setRevenue(company.getRevenue().add(dto.price()));
            companyRepository.save(company);
        }

        transport.setStartLocation(dto.startLocation());
        transport.setEndLocation(dto.endLocation());
        transport.setStartDate(dto.startDate());
        transport.setEndDate(dto.endDate());
        transport.setPrice(dto.price());
        transport.setPaid(dto.paid());
        transport.setTransportType(dto.transportType());
        transport.setCargoWeight(dto.cargoWeight());
        transport.setPassengersCount(dto.passengersCount());

        Transport saved = transportRepository.save(transport);

        TransportResponseDto responseDto = transportMapper.toResponseDto(saved);

        saveTransportToFile(responseDto);

        return responseDto;
    }
    @Override
    public TransportResponseDto updateTransport(Integer id, TransportDto dto) {
        Transport existing = transportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transport not found with id " + id));

        if (dto.startLocation() != null) {
            existing.setStartLocation(dto.startLocation());
        }

        if (dto.endLocation() != null) {
            existing.setEndLocation(dto.endLocation());
        }

        if (dto.startDate() != null) {
            existing.setStartDate(dto.startDate());
        }

        if (dto.endDate() != null) {
            existing.setEndDate(dto.endDate());
        }

        if (dto.price() != null) {
            existing.setPrice(dto.price());
        }

        boolean wasPaid = existing.isPaid();
        if (dto.paid() != null) {
            existing.setPaid(dto.paid());
        }

        if (dto.transportType() != null) {
            existing.setTransportType(dto.transportType());
        }

        if (dto.cargoWeight() != null) {
            existing.setCargoWeight(dto.cargoWeight());
        }

        if (dto.passengersCount() != null) {
            existing.setPassengersCount(dto.passengersCount());
        }

        if (dto.vehicleId() != null) {
            Vehicle v = vehicleRepository.findById(dto.vehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + dto.vehicleId()));
            existing.setVehicle(v);
        }

        if (dto.driverId() != null) {
            Employee drv = employeeRepository.findById(dto.driverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id " + dto.driverId()));
            existing.setDriver(drv);
        }

        if (dto.clientId() != null) {
            Client c = clientRepository.findById(dto.clientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + dto.clientId()));
            existing.setClient(c);
        }

        if (dto.statusId() != null) {
            TransportStatus st = transportStatusRepository.findById(dto.statusId())
                    .orElseThrow(() -> new ResourceNotFoundException("TransportStatus not found with id " + dto.statusId()));
            existing.setStatus(st);
        }

        if (dto.companyId() != null) {
            TransportCompany comp = companyRepository.findById(dto.companyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + dto.companyId()));
            existing.setCompany(comp);
        }

        validateTransportType(dto);
        checkVehicleType(existing.getTransportType(), existing.getPassengersCount(), existing.getVehicle());
        checkDriverQualifications(existing.getTransportType(), existing.getCargoWeight(), existing.getPassengersCount(), existing.getDriver());

        if (!wasPaid && Boolean.TRUE.equals(dto.paid())) {
            if (existing.getCompany().getRevenue() == null) {
                existing.getCompany().setRevenue(BigDecimal.ZERO);
            }
            existing.getCompany().setRevenue(existing.getCompany().getRevenue().add(existing.getPrice()));
            companyRepository.save(existing.getCompany());
        }

        Transport updated = transportRepository.save(existing);
        return transportMapper.toResponseDto(updated);
    }
    @Override
    public TransportResponseDto getTransportById(Integer id) {
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transport not found with id " + id));
        return transportMapper.toResponseDto(transport);
    }
    @Override
    public List<TransportResponseDto> getAllTransports() {
        return transportRepository.findAll()
                .stream()
                .map(transportMapper::toResponseDto)
                .toList();
    }
    @Override
    @Transactional
    public void deleteTransport(Integer id) {
        Transport t = transportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transport not found with id " + id));
        transportRepository.delete(t);
    }
    @Override
    public List<TransportResponseDto> getTransportsByEndLocation(String endLocation) {
        return transportRepository.findByEndLocation(endLocation)
                .stream()
                .map(transportMapper::toResponseDto)
                .toList();
    }
    @Override
    public List<TransportResponseDto> getPaidTransports() {
        return transportRepository.findByPaidTrue()
                .stream()
                .map(transportMapper::toResponseDto)
                .toList();
    }
    @Override
    public List<TransportResponseDto> getUnpaidTransports() {
        return transportRepository.findByPaidFalse()
                .stream()
                .map(transportMapper::toResponseDto)
                .toList();
    }
    @Override
    public List<TransportResponseDto> getTransportsInDateRange(LocalDateTime start, LocalDateTime end) {
        return transportRepository.findByStartDateBetween(start, end)
                .stream()
                .map(transportMapper::toResponseDto)
                .toList();
    }
    @Override
    public Long countAllTransports() {
        return transportRepository.countAllTransports();
    }
    @Override
    public BigDecimal sumAllTransportPrices() {
        return transportRepository.sumAllTransportPrices();
    }
    @Override
    public List<Object[]> countTransportsByAllDrivers() {
        return transportRepository.countTransportsByAllDrivers();
    }
    @Override
    public Long countTransportsByDriver(Integer driverId) {return transportRepository.countTransportsByDriverId(driverId);}
    @Override
    public List<Object[]> sumPricesByDriver() {
        return transportRepository.sumPricesByDriver();
    }
    @Override
    public DriverTransportSummaryDto getDriverTransportSummary(Integer driverId) {
        Employee driver = employeeRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id " + driverId));

        List<Transport> transports = transportRepository.findByDriverId(driverId);

        BigDecimal totalPrice = transports.stream()
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return transportMapper.toDriverTransportSummaryDto(driver, totalPrice);
    }

    private void validateTransportType(TransportDto dto) {
        if (dto.transportType() == TransportType.CARGO && (dto.cargoWeight() == null || dto.cargoWeight().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("CARGO transport requires cargoWeight > 0");
        }
    }

    private void checkDriverQualifications(TransportType type, BigDecimal cargoWeight, Integer passengersCount, Employee driver) {
        List<String> driverQualNames = driver.getQualifications().stream()
                .map(Qualification::getName)
                .toList();

        switch (type) {
            case PASSENGERS -> {
                if (passengersCount != null && passengersCount > 12 && !driverQualNames.contains("PASSENGERS_OVER_12")) {
                    throw new IllegalArgumentException("Driver does not have PASSENGERS_OVER_12 qualification for more than 12 passengers.");
                }
            }
            case CARGO -> {
                if (!driverQualNames.contains("HEAVY_CARGO")) {
                    throw new IllegalArgumentException("Driver does not have HEAVY_CARGO qualification for cargo transport.");
                }
            }
            case HAZMAT -> {
                if (!driverQualNames.contains("HAZMAT")) {
                    throw new IllegalArgumentException("Driver does not have HAZMAT qualification for HAZMAT transport.");
                }
            }
        }
    }

    private void checkVehicleType(TransportType type, Integer passengersCount, Vehicle vehicle) {
        String vType = vehicle.getVehicleType().getName();

        switch (type) {
            case PASSENGERS -> {
                if (!"BUS".equalsIgnoreCase(vType)) {
                    throw new IllegalArgumentException("For PASSENGERS transport, vehicle type must be BUS.");
                }
            }
            case CARGO -> {
                if (!"TRUCK".equalsIgnoreCase(vType)) {
                    throw new IllegalArgumentException("For CARGO transport, vehicle type must be TRUCK.");
                }
            }
            case HAZMAT -> {
                if (!"CISTERN".equalsIgnoreCase(vType)) {
                    throw new IllegalArgumentException("For HAZMAT transport, vehicle type must be CISTERN.");
                }
            }
        }
    }

    private List<TransportResponseDto> readTransportsFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, TransportResponseDto.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read transports from file: " + e.getMessage(), e);
        }
    }

    public void saveTransportToFile(TransportResponseDto transport) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();

            List<TransportResponseDto> existingTransports = readTransportsFromFile();
            existingTransports.add(transport);

            writer.writeValue(file, existingTransports);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save transport to file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TransportResponseDto> getAllTransportsFromFile() {return readTransportsFromFile();}
}