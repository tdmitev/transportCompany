package com.example.transportCompany.dto;

import java.math.BigDecimal;
public record VehicleResponseDto(
        Integer id,
        String licensePlate,
        BigDecimal fuelConsumption,
        Integer companyId,
        String typeName
) {}
