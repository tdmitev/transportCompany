package com.example.transportCompany.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientDto(
        Integer id,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address can not be longer than 255 characters")
        String address,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "\\d{10,15}", message = "Phone number must be valid and contain 10-15 digits")
        String phone
) {}
