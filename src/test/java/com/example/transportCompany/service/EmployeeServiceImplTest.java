package com.example.transportCompany.service;

import com.example.transportCompany.dto.EmployeeDto;
import com.example.transportCompany.dto.EmployeeResponseDto;
import com.example.transportCompany.dto.QualificationDto;
import com.example.transportCompany.mapper.EmployeeMapper;
import com.example.transportCompany.model.Employee;
import com.example.transportCompany.model.Qualification;
import com.example.transportCompany.model.TransportCompany;
import com.example.transportCompany.repository.EmployeeRepository;
import com.example.transportCompany.repository.QualificationRepository;
import com.example.transportCompany.repository.TransportCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TransportCompanyRepository companyRepository;

    @Mock
    private QualificationRepository qualificationRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private TransportCompany mockCompany;
    private Qualification mockQualification1;
    private Qualification mockQualification2;
    private Employee mockEmployee1;
    private Employee mockEmployee2;
    private QualificationDto qualificationDto1;
    private QualificationDto qualificationDto2;

    @BeforeEach
    void setUp() {
        mockCompany = new TransportCompany();
        mockCompany.setId(1);
        mockCompany.setName("Test Company");

        mockQualification1 = new Qualification();
        mockQualification1.setId(1);
        mockQualification1.setName("HEAVY_CARGO");

        mockQualification2 = new Qualification();
        mockQualification2.setId(2);
        mockQualification2.setName("MANAGER");

        mockEmployee1 = new Employee();
        mockEmployee1.setId(1);
        mockEmployee1.setName("John Doe");
        mockEmployee1.setPosition("Driver");
        mockEmployee1.setSalary(BigDecimal.valueOf(2000));
        mockEmployee1.setCompany(mockCompany);
        mockEmployee1.setQualifications(List.of(mockQualification1));

        mockEmployee2 = new Employee();
        mockEmployee2.setId(2);
        mockEmployee2.setName("Jane Doe");
        mockEmployee2.setPosition("Manager");
        mockEmployee2.setSalary(BigDecimal.valueOf(3000));
        mockEmployee2.setCompany(mockCompany);
        mockEmployee2.setQualifications(List.of(mockQualification2));

        qualificationDto1 = new QualificationDto(1, "HEAVY_CARGO");
        qualificationDto2 = new QualificationDto(2, "MANAGER");
    }

    @Test
    void testCreateEmployeeSuccess() {
        EmployeeDto employeeDto = new EmployeeDto(
                null,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                List.of(1, 2),
                null
        );

        when(companyRepository.findById(1)).thenReturn(Optional.of(mockCompany));
        when(qualificationRepository.findAllById(List.of(1, 2))).thenReturn(List.of(mockQualification1, mockQualification2));
        when(employeeMapper.toEntity(employeeDto)).thenReturn(mockEmployee1);
        when(employeeRepository.save(mockEmployee1)).thenReturn(mockEmployee1);
        when(employeeMapper.toResponseDto(mockEmployee1)).thenReturn(new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(qualificationDto1, qualificationDto2)
        ));

        EmployeeResponseDto response = employeeService.createEmployee(employeeDto);

        verify(companyRepository).findById(1);
        verify(qualificationRepository).findAllById(List.of(1, 2));
        verify(employeeRepository).save(mockEmployee1);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("Driver", response.position());
        assertEquals(BigDecimal.valueOf(2000), response.salary());
        assertEquals("Test Company", response.companyName());
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee1));
        when(employeeMapper.toResponseDto(mockEmployee1)).thenReturn(new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(qualificationDto1)
        ));

        EmployeeResponseDto response = employeeService.getEmployeeById(1);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("Driver", response.position());
        assertEquals(BigDecimal.valueOf(2000), response.salary());
        assertEquals("Test Company", response.companyName());
        assertEquals(1, response.qualifications().size());
        assertEquals("HEAVY_CARGO", response.qualifications().get(0).name());
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(mockEmployee1, mockEmployee2));
        when(employeeMapper.toResponseDto(mockEmployee1)).thenReturn(new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(qualificationDto1)
        ));
        when(employeeMapper.toResponseDto(mockEmployee2)).thenReturn(new EmployeeResponseDto(
                2,
                "Jane Doe",
                "Manager",
                BigDecimal.valueOf(3000),
                1,
                "Test Company",
                List.of(qualificationDto2)
        ));

        List<EmployeeResponseDto> response = employeeService.getAllEmployees();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("John Doe", response.get(0).name());
        assertEquals("Jane Doe", response.get(1).name());
    }

    @Test
    void testGetEmployeesByCompanyId() {
        when(employeeRepository.findByCompanyId(1)).thenReturn(List.of(mockEmployee1, mockEmployee2));
        when(employeeMapper.toResponseDto(mockEmployee1)).thenReturn(new EmployeeResponseDto(
                1,
                "John Doe",
                "Driver",
                BigDecimal.valueOf(2000),
                1,
                "Test Company",
                List.of(qualificationDto1)
        ));
        when(employeeMapper.toResponseDto(mockEmployee2)).thenReturn(new EmployeeResponseDto(
                2,
                "Jane Doe",
                "Manager",
                BigDecimal.valueOf(3000),
                1,
                "Test Company",
                List.of(qualificationDto2)
        ));

        List<EmployeeResponseDto> response = employeeService.getEmployeesByCompanyId(1);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("John Doe", response.get(0).name());
        assertEquals("Jane Doe", response.get(1).name());
    }

    @Test
    void testUpdateEmployee() {
        EmployeeDto updateDto = new EmployeeDto(
                null,
                "Jane Doe",
                "Manager",
                BigDecimal.valueOf(3000),
                1,
                List.of(1),
                null
        );

        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee1));
        when(companyRepository.findById(1)).thenReturn(Optional.of(mockCompany));
        when(qualificationRepository.findAllById(List.of(1))).thenReturn(List.of(mockQualification1));
        when(employeeRepository.save(mockEmployee1)).thenReturn(mockEmployee1);
        when(employeeMapper.toResponseDto(mockEmployee1)).thenReturn(new EmployeeResponseDto(
                1,
                "Jane Doe",
                "Manager",
                BigDecimal.valueOf(3000),
                1,
                "Test Company",
                List.of(qualificationDto1)
        ));

        EmployeeResponseDto response = employeeService.updateEmployee(1, updateDto);

        verify(employeeRepository).findById(1);
        verify(companyRepository).findById(1);
        verify(qualificationRepository).findAllById(List.of(1));
        verify(employeeRepository).save(mockEmployee1);

        assertNotNull(response);
        assertEquals("Jane Doe", response.name());
        assertEquals("Manager", response.position());
        assertEquals(BigDecimal.valueOf(3000), response.salary());
    }

    @Test
    void testDeleteEmployee() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee1));
        doNothing().when(employeeRepository).delete(mockEmployee1);

        employeeService.deleteEmployee(1);

        verify(employeeRepository).delete(mockEmployee1);
    }
}