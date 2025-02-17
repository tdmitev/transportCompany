package com.example.transportCompany.dto;

import com.example.transportCompany.model.TransportType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransportDto(
        Integer id,

        @NotBlank(message = "Start location is required")
        @Size(max = 45, message = "Start location too long")
        String startLocation,

        @NotBlank(message = "End location is required")
        @Size(max = 45, message = "End location too long")
        String endLocation,

        @NotNull(message = "Start date is required")
        LocalDateTime startDate,

        @NotNull(message = "End date is required")
        LocalDateTime endDate,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price must be non-negative")
        BigDecimal price,

        @NotNull(message = "Transport type is required")
        TransportType transportType,

        @DecimalMin(value="0.0", message="Cargo weight must be non-negative")
        BigDecimal cargoWeight,

        @Min(value=0, message="Passengers count must be non-negative")
        Integer passengersCount,

        @NotNull(message = "Vehicle ID is required")
        Integer vehicleId,

        @NotNull(message = "Driver (Employee) ID is required")
        Integer driverId,

        @NotNull(message = "Client ID is required")
        Integer clientId,

        @NotNull(message = "Status ID is required")
        Integer statusId,

        @NotNull(message = "Company ID is required")
        Integer companyId,

        Boolean paid
) {}