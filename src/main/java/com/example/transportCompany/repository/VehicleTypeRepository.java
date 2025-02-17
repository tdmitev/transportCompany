package com.example.transportCompany.repository;

import com.example.transportCompany.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {

    Optional<VehicleType> findByName(String name);

}