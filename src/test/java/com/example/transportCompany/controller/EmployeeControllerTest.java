package com.example.transportCompany.controller;

import com.example.transportCompany.dto.EmployeeDto;
import com.example.transportCompany.dto.EmployeeResponseDto;
import com.example.transportCompany.dto.QualificationDto;
import com.example.transportCompany.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(
                null,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                List.of(1, 2),
                null
        );

        EmployeeResponseDto responseDto = new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(new QualificationDto(1, "HEAVY_CARGO"), new QualificationDto(2, "HAZMAT"))
        );

        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(responseDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.position").value("Driver"))
                .andExpect(jsonPath("$.salary").value(2000))
                .andExpect(jsonPath("$.companyId").value(1))
                .andExpect(jsonPath("$.companyName").value("Test Company"))
                .andExpect(jsonPath("$.qualifications[0].name").value("HEAVY_CARGO"));
    }

    @Test
    void testGetEmployee() throws Exception {
        EmployeeResponseDto responseDto = new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(new QualificationDto(1, "HEAVY_CARGO"))
        );

        when(employeeService.getEmployeeById(1)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.position").value("Driver"))
                .andExpect(jsonPath("$.salary").value(2000))
                .andExpect(jsonPath("$.companyId").value(1))
                .andExpect(jsonPath("$.companyName").value("Test Company"))
                .andExpect(jsonPath("$.qualifications[0].name").value("HEAVY_CARGO"));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeResponseDto employee1 = new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(new QualificationDto(1, "HEAVY_CARGO"))
        );

        EmployeeResponseDto employee2 = new EmployeeResponseDto(
                2,
                "Jane Doe",
                "Manager",
                BigDecimal.valueOf(3000),
                1,
                "Test Company",
                List.of(new QualificationDto(2, "MANAGER"))
        );

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee1, employee2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void testGetEmployeesByCompanyId() throws Exception {
        EmployeeResponseDto employee1 = new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(new QualificationDto(1, "HEAVY_CARGO"))
        );

        when(employeeService.getEmployeesByCompanyId(1)).thenReturn(List.of(employee1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/by-company/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeDto updateDto = new EmployeeDto(
                null,
                "Jane Doe",
                "Manager",
                BigDecimal.valueOf(3000),
                1,
                List.of(1),
                null
        );

        EmployeeResponseDto responseDto = new EmployeeResponseDto(
                1,
                "Jane Doe",
                "Manager",
                BigDecimal.valueOf(3000),
                1,
                "Test Company",
                List.of(new QualificationDto(1, "MANAGER"))
        );

        when(employeeService.updateEmployee(eq(1), any(EmployeeDto.class))).thenReturn(responseDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String updateDtoJson = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.position").value("Manager"))
                .andExpect(jsonPath("$.salary").value(3000));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }
}