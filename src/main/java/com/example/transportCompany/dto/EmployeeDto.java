package com.example.transportCompany.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record EmployeeDto(
        Integer id,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @Size(max = 45, message = "Position can not be longer than 45 characters")
        String position,

        @NotNull(message = "Salary is required")
        @Min(value = 0, message = "Salary must be non-negative")
        BigDecimal salary,

        @NotNull(message = "Company ID is required")
        Integer companyId,

        List<Integer> qualificationIds,

        List<QualificationDto> qualifications
) {
}
