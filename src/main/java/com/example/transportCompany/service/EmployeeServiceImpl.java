package com.example.transportCompany.service;

import com.example.transportCompany.dto.EmployeeDto;
import com.example.transportCompany.dto.EmployeeResponseDto;
import com.example.transportCompany.exception.ResourceNotFoundException;
import com.example.transportCompany.mapper.EmployeeMapper;
import com.example.transportCompany.model.Employee;
import com.example.transportCompany.model.Qualification;
import com.example.transportCompany.model.TransportCompany;
import com.example.transportCompany.repository.EmployeeRepository;
import com.example.transportCompany.repository.QualificationRepository;
import com.example.transportCompany.repository.TransportCompanyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TransportCompanyRepository companyRepository;
    private final QualificationRepository qualificationRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               TransportCompanyRepository companyRepository,
                               QualificationRepository qualificationRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.qualificationRepository = qualificationRepository;
        this.employeeMapper = employeeMapper;
    }
    @Override
    public EmployeeResponseDto createEmployee(EmployeeDto dto) {

        Employee employee = employeeMapper.toEntity(dto);

        TransportCompany company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + dto.companyId()));
        employee.setCompany(company);

        if (dto.qualificationIds() != null) {
            List<Qualification> qualifications = qualificationRepository.findAllById(dto.qualificationIds());
            if (qualifications.size() != dto.qualificationIds().size()) {
                throw new ResourceNotFoundException("Some qualifications not found");
            }
            employee.setQualifications(qualifications);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDto(savedEmployee);
    }
    @Override
    public EmployeeResponseDto getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        return employeeMapper.toResponseDto(employee);
    }
    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<EmployeeResponseDto> getEmployeesByCompanyId(Integer companyId) {
        List<Employee> employees = employeeRepository.findByCompanyId(companyId);

        return employees.stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<EmployeeResponseDto> getSortedAndFilteredEmployees(String qualification, BigDecimal minSalary, BigDecimal maxSalary, String sortOrder) {
        List<Employee> employees;

        if (qualification != null && !qualification.isEmpty()) {
            employees = employeeRepository.findByQualificationName(qualification);
        } else if (minSalary != null && maxSalary != null) {
            employees = employeeRepository.findBySalaryBetween(minSalary, maxSalary);
        } else if (minSalary != null) {
            employees = employeeRepository.findBySalaryGreaterThan(minSalary);
        } else {
            employees = employeeRepository.findAll();
        }

        if ("asc".equalsIgnoreCase(sortOrder)) {
            employees = employees.stream()
                    .sorted(Comparator.comparing(Employee::getSalary))
                    .toList();
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            employees = employees.stream()
                    .sorted(Comparator.comparing(Employee::getSalary).reversed())
                    .toList();
        }

        return employees.stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Override
    public EmployeeResponseDto updateEmployee(Integer id, EmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        existing.setName(dto.name());
        existing.setPosition(dto.position());
        existing.setSalary(dto.salary());

        if (dto.companyId() != null) {
            TransportCompany company = companyRepository.findById(dto.companyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + dto.companyId()));
            existing.setCompany(company);
        }

        List<Qualification> qualifications = loadQualifications(dto.qualificationIds());
        existing.setQualifications(qualifications);

        existing = employeeRepository.save(existing);

        return employeeMapper.toResponseDto(existing);
    }
    @Override
    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        employeeRepository.delete(employee);
    }

    private List<Qualification> loadQualifications(List<Integer> qIds) {
        if (qIds == null || qIds.isEmpty()) {
            return new ArrayList<>();
        }

        return qualificationRepository.findAllById(qIds);
    }
}