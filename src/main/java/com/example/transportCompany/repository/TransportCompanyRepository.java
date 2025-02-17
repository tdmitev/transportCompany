package com.example.transportCompany.repository;

import com.example.transportCompany.model.TransportCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportCompanyRepository extends JpaRepository<TransportCompany, Integer> {

    List<TransportCompany> findAllByOrderByNameAsc();

    List<TransportCompany> findAllByOrderByNameDesc();

    List<TransportCompany> findAllByOrderByRevenueDesc();

    List<TransportCompany> findByNameContainingIgnoreCase(String partialName);
}