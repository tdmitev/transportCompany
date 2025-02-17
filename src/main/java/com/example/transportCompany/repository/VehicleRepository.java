package com.example.transportCompany.repository;

import com.example.transportCompany.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

}