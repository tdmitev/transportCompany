package com.example.transportCompany.controller;

import com.example.transportCompany.dto.DriverTransportSummaryDto;
import com.example.transportCompany.dto.TransportDto;
import com.example.transportCompany.dto.TransportResponseDto;
import com.example.transportCompany.service.TransportService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transports")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @PostMapping
    public ResponseEntity<TransportResponseDto> createTransport(@Valid @RequestBody TransportDto dto) {
        TransportResponseDto createdTransport = transportService.createTransport(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportResponseDto> updateTransport(@PathVariable Integer id, @Valid @RequestBody TransportDto dto) {
        TransportResponseDto updatedTransport = transportService.updateTransport(id, dto);
        return ResponseEntity.ok(updatedTransport);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportResponseDto> getTransportById(@PathVariable Integer id) {
        TransportResponseDto transport = transportService.getTransportById(id);
        return ResponseEntity.ok(transport);
    }

    @GetMapping
    public ResponseEntity<List<TransportResponseDto>> getAllTransports() {
        List<TransportResponseDto> transports = transportService.getAllTransports();
        return ResponseEntity.ok(transports);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable Integer id) {
        transportService.deleteTransport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/file")
    public ResponseEntity<List<TransportResponseDto>> getTransportsFromFile() {
        List<TransportResponseDto> transports = transportService.getAllTransportsFromFile();
        return ResponseEntity.ok(transports);
    }

    @GetMapping("/by-end-location")
    public ResponseEntity<List<TransportResponseDto>> getTransportsByEndLocation(@RequestParam String endLocation) {
        List<TransportResponseDto> transports = transportService.getTransportsByEndLocation(endLocation);
        return ResponseEntity.ok(transports);
    }

    @GetMapping("/paid")
    public ResponseEntity<List<TransportResponseDto>> getPaidTransports() {
        List<TransportResponseDto> transports = transportService.getPaidTransports();
        return ResponseEntity.ok(transports);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<TransportResponseDto>> getUnpaidTransports() {
        List<TransportResponseDto> transports = transportService.getUnpaidTransports();
        return ResponseEntity.ok(transports);
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<TransportResponseDto>> getTransportsInDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TransportResponseDto> transports = transportService.getTransportsInDateRange(start, end);
        return ResponseEntity.ok(transports);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllTransports() {
        Long count = transportService.countAllTransports();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/sum-prices")
    public ResponseEntity<BigDecimal> sumAllTransportPrices() {
        BigDecimal total = transportService.sumAllTransportPrices();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/count-by-all-drivers")
    public ResponseEntity<List<Object[]>> countTransportsByAllDrivers() {
        List<Object[]> results = transportService.countTransportsByAllDrivers();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/count-by-driver/{driverId}")
    public ResponseEntity<Long> countTransportsByDriver(@PathVariable Integer driverId) {
        Long count = transportService.countTransportsByDriver(driverId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/sum-prices-by-all-drivers")
    public ResponseEntity<List<Object[]>> sumPricesByDriver() {
        List<Object[]> results = transportService.sumPricesByDriver();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/sum-prices-by-driver")
    public ResponseEntity<DriverTransportSummaryDto> sumPricesByDriver(@RequestParam Integer driverId) {
        DriverTransportSummaryDto result = transportService.getDriverTransportSummary(driverId);
        return ResponseEntity.ok(result);
    }
}

