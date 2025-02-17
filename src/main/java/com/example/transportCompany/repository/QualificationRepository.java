package com.example.transportCompany.repository;

import com.example.transportCompany.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Integer> {

    Optional<Qualification> findByName(String name);

}