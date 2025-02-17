package com.example.transportCompany.dto;

import java.math.BigDecimal;
import java.util.List;

public record EmployeeResponseDto(
        Integer id,
        String name,
        String position,
        BigDecimal salary,
        Integer companyId,
        String companyName,
        List<QualificationDto> qualifications
) {}