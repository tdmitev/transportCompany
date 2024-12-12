package com.example.transportCompany.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record VehicleDto(
        Integer id,
        @NotBlank(message = "License plate is required")
        @Size(min = 2, max = 20, message = "License plate must be between 2 and 20 characters")
        String licensePlate,

        @NotNull(message = "Fuel consumption is required")
        @DecimalMin(value = "0.01", message = "Fuel consumption must be positive")
        BigDecimal fuelConsumption,

        @NotNull(message = "Company ID is required")
        Integer companyId,

        @NotNull(message = "Vehicle type ID is required")
        Integer typeId
) {}