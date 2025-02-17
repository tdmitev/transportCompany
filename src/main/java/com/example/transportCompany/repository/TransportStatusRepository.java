package com.example.transportCompany.repository;

import com.example.transportCompany.model.TransportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportStatusRepository extends JpaRepository<TransportStatus, Integer> {

    Optional<TransportStatus> findByName(String name);

}