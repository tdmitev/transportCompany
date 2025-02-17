package com.example.transportCompany.dto;

import java.math.BigDecimal;

public record DriverTransportSummaryDto(
        Integer driverId,
        String driverName,
        BigDecimal totalPrice
) {}