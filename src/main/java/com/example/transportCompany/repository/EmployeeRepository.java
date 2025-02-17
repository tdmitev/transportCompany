package com.example.transportCompany.repository;

import com.example.transportCompany.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findBySalaryGreaterThan(BigDecimal salary);

    List<Employee> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);

    @Query("SELECT e FROM Employee e JOIN e.qualifications q WHERE q.name = :qName")
    List<Employee> findByQualificationName(@Param("qName") String qName);
    List<Employee> findByCompanyId(Integer companyId);

    List<Employee> findAllByOrderBySalaryAsc();
}