package com.example.transportCompany.controller;

import com.example.transportCompany.dto.TransportCompanyDto;
import com.example.transportCompany.service.TransportCompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class TransportCompanyController {

    private final TransportCompanyService transportCompanyService;

    public TransportCompanyController(TransportCompanyService transportCompanyService) {
        this.transportCompanyService = transportCompanyService;
    }

    @PostMapping
    public ResponseEntity<TransportCompanyDto> createCompany(@Valid @RequestBody TransportCompanyDto dto) {
        TransportCompanyDto created = transportCompanyService.createCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportCompanyDto> getCompany(@PathVariable Integer id) {
        TransportCompanyDto dto = transportCompanyService.getCompanyById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<TransportCompanyDto>> getAllCompanies() {
        List<TransportCompanyDto> companies = transportCompanyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportCompanyDto> updateCompany(@PathVariable Integer id, @Valid @RequestBody TransportCompanyDto dto) {
        TransportCompanyDto updated = transportCompanyService.updateCompany(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        transportCompanyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}