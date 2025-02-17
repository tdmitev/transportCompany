package com.example.transportCompany.dto;

import com.example.transportCompany.model.TransportType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransportResponseDto(
        Integer id,
        String startLocation,
        String endLocation,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        TransportType transportType,
        BigDecimal cargoWeight,
        Integer passengersCount,
        boolean paid,

        Integer vehicleId,
        String vehicleLicensePlate,

        Integer driverId,
        String driverName,

        Integer clientId,
        String clientName,

        Integer companyId,
        String companyName,

        Integer statusId,
        String statusName
) {}