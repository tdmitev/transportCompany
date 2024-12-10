package com.example.transportCompany.repository;

import com.example.transportCompany.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findByNameContainingIgnoreCase(String partialName);

    Optional<Client> findByEmail(String email);
}