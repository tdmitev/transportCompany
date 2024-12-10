package com.example.transportCompany.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record TransportCompanyDto (
        Integer id,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        BigDecimal revenue,

        @Size(max = 255, message = "Address can not be longer than 255 characters")
        String address,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Contact email is required")
        String contactEmail,

        @NotBlank(message = "Contact phone is required")
        @Pattern(regexp = "\\d{10,15}", message = "Phone number must be valid and contain 10-15 digits")
        String contactPhone
)
{}
