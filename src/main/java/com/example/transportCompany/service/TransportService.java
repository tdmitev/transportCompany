package com.example.transportCompany.service;

import com.example.transportCompany.dto.TransportDto;
import com.example.transportCompany.dto.TransportResponseDto;
import com.example.transportCompany.dto.DriverTransportSummaryDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransportService {
    TransportResponseDto createTransport(TransportDto dto);
    TransportResponseDto updateTransport(Integer id, TransportDto dto);
    TransportResponseDto getTransportById(Integer id);
    List<TransportResponseDto> getAllTransports();
    void deleteTransport(Integer id);
    List<TransportResponseDto> getTransportsByEndLocation(String endLocation);
    List<TransportResponseDto> getPaidTransports();
    List<TransportResponseDto> getUnpaidTransports();
    List<TransportResponseDto> getTransportsInDateRange(LocalDateTime start, LocalDateTime end);
    Long countAllTransports();
    BigDecimal sumAllTransportPrices();
    List<Object[]> countTransportsByAllDrivers();

    Long countTransportsByDriver(Integer driverId);

    List<Object[]> sumPricesByDriver();
    DriverTransportSummaryDto getDriverTransportSummary(Integer driverId);
    List<TransportResponseDto> getAllTransportsFromFile();
}