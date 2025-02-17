package com.example.transportCompany.repository;

import com.example.transportCompany.model.Employee;
import com.example.transportCompany.model.Qualification;
import com.example.transportCompany.model.TransportCompany;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private TransportCompany mockCompany;
    private Qualification qualification1;
    private Qualification qualification2;

    @BeforeEach
    void setUp() {
        mockCompany = new TransportCompany();
        mockCompany.setName("Test Company");
        mockCompany = entityManager.persistAndFlush(mockCompany);

        qualification1 = new Qualification();
        qualification1.setName("HEAVY_CARGO");
        qualification1 = entityManager.persistAndFlush(qualification1);

        qualification2 = new Qualification();
        qualification2.setName("MANAGER");
        qualification2 = entityManager.persistAndFlush(qualification2);

        Employee employee1 = new Employee();
        employee1.setName("John Doe");
        employee1.setPosition("Driver");
        employee1.setSalary(BigDecimal.valueOf(2000));
        employee1.setCompany(mockCompany);
        employee1.setQualifications(List.of(qualification1));
        entityManager.persistAndFlush(employee1);

        Employee employee2 = new Employee();
        employee2.setName("Jane Doe");
        employee2.setPosition("Manager");
        employee2.setSalary(BigDecimal.valueOf(3000));
        employee2.setCompany(mockCompany);
        employee2.setQualifications(List.of(qualification2));
        entityManager.persistAndFlush(employee2);

        Employee employee3 = new Employee();
        employee3.setName("Alice Smith");
        employee3.setPosition("Assistant");
        employee3.setSalary(BigDecimal.valueOf(1500));
        employee3.setCompany(mockCompany);
        employee3.setQualifications(List.of(qualification1, qualification2));
        entityManager.persistAndFlush(employee3);
    }

    @Test
    void testFindBySalaryGreaterThan() {
        List<Employee> result = employeeRepository.findBySalaryGreaterThan(BigDecimal.valueOf(1800));
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("John Doe")));
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Jane Doe")));
    }

    @Test
    void testFindBySalaryBetween() {
        List<Employee> result = employeeRepository.findBySalaryBetween(BigDecimal.valueOf(1800), BigDecimal.valueOf(2500));
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testFindByQualificationName() {
        List<Employee> result = employeeRepository.findByQualificationName("HEAVY_CARGO");
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("John Doe")));
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Alice Smith")));
    }

    @Test
    void testFindByCompanyId() {
        List<Employee> result = employeeRepository.findByCompanyId(mockCompany.getId());
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("John Doe")));
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Jane Doe")));
        assertTrue(result.stream().anyMatch(e -> e.getName().equals("Alice Smith")));
    }

    @Test
    void testFindAllByOrderBySalaryAsc() {
        List<Employee> result = employeeRepository.findAllByOrderBySalaryAsc();
        assertEquals(3, result.size());
        assertEquals("Alice Smith", result.get(0).getName());
        assertEquals("John Doe", result.get(1).getName());
        assertEquals("Jane Doe", result.get(2).getName());
    }
}