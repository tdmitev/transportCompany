package com.example.transportCompany.service;

import com.example.transportCompany.dto.EmployeeDto;
import com.example.transportCompany.dto.EmployeeResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeDto dto);
    EmployeeResponseDto getEmployeeById(Integer id);
    List<EmployeeResponseDto> getAllEmployees();

    List<EmployeeResponseDto> getEmployeesByCompanyId(Integer companyId);

    List<EmployeeResponseDto> getSortedAndFilteredEmployees(String qualification, BigDecimal minSalary, BigDecimal maxSalary, String sortOrder);

    EmployeeResponseDto updateEmployee(Integer id, EmployeeDto dto);
    void deleteEmployee(Integer id);
}
