package com.example.transportCompany.service;

import com.example.transportCompany.dto.TransportCompanyDto;
import com.example.transportCompany.exception.ResourceNotFoundException;
import com.example.transportCompany.mapper.TransportCompanyMapper;
import com.example.transportCompany.model.TransportCompany;
import com.example.transportCompany.repository.TransportCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportCompanyService {

    private final TransportCompanyRepository transportCompanyRepository;
    private final TransportCompanyMapper transportCompanyMapper;

    public TransportCompanyService(TransportCompanyRepository transportCompanyRepository,
                                   TransportCompanyMapper transportCompanyMapper) {
        this.transportCompanyRepository = transportCompanyRepository;
        this.transportCompanyMapper = transportCompanyMapper;
    }

    public TransportCompanyDto createCompany(TransportCompanyDto dto) {
        TransportCompany company = transportCompanyMapper.toEntity(dto);
        company = transportCompanyRepository.save(company);
        return transportCompanyMapper.toDto(company);
    }

    public TransportCompanyDto getCompanyById(Integer id) {
        TransportCompany company = transportCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransportCompany not found with id " + id));
        return transportCompanyMapper.toDto(company);
    }

    public List<TransportCompanyDto> getAllCompanies() {
        return transportCompanyRepository.findAll()
                .stream()
                .map(transportCompanyMapper::toDto)
                .toList();
    }

    public TransportCompanyDto updateCompany(Integer id, TransportCompanyDto dto) {
        TransportCompany existingCompany = transportCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransportCompany not found with id " + id));

        existingCompany.setName(dto.name());
        existingCompany.setRevenue(dto.revenue());
        existingCompany.setAddress(dto.address());
        existingCompany.setContactEmail(dto.contactEmail());
        existingCompany.setContactPhone(dto.contactPhone());

        existingCompany = transportCompanyRepository.save(existingCompany);
        return transportCompanyMapper.toDto(existingCompany);
    }

    public void deleteCompany(Integer id) {
        TransportCompany company = transportCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransportCompany not found with id " + id));
        transportCompanyRepository.delete(company);
    }
}