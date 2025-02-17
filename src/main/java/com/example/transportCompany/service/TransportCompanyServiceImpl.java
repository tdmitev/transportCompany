package com.example.transportCompany.service;

import com.example.transportCompany.dto.TransportCompanyDto;
import com.example.transportCompany.exception.ResourceNotFoundException;
import com.example.transportCompany.mapper.TransportCompanyMapper;
import com.example.transportCompany.model.TransportCompany;
import com.example.transportCompany.repository.TransportCompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TransportCompanyServiceImpl implements TransportCompanyService{

    private final TransportCompanyRepository transportCompanyRepository;
    private final TransportCompanyMapper transportCompanyMapper;

    public TransportCompanyServiceImpl(TransportCompanyRepository transportCompanyRepository,
                                       TransportCompanyMapper transportCompanyMapper) {
        this.transportCompanyRepository = transportCompanyRepository;
        this.transportCompanyMapper = transportCompanyMapper;
    }
    @Override
    public TransportCompanyDto createCompany(TransportCompanyDto dto) {
        TransportCompany company = transportCompanyMapper.toEntity(dto);
        company = transportCompanyRepository.save(company);
        return transportCompanyMapper.toDto(company);
    }
    @Override
    public TransportCompanyDto getCompanyById(Integer id) {
        TransportCompany company = transportCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransportCompany not found with id " + id));
        return transportCompanyMapper.toDto(company);
    }
    @Override
    public List<TransportCompanyDto> getAllCompanies() {
        return transportCompanyRepository.findAll()
                .stream()
                .map(transportCompanyMapper::toDto)
                .toList();
    }
    @Override
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
    @Override
    @Transactional
    public void deleteCompany(Integer id) {
        TransportCompany company = transportCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransportCompany not found with id " + id));
        transportCompanyRepository.delete(company);
    }
    @Override
    public List<TransportCompanyDto> getSortedAndFilteredCompanies(String sortBy, String order, String partialName) {
        List<TransportCompany> companies;

        if (partialName != null && !partialName.isEmpty()) {
            companies = transportCompanyRepository.findByNameContainingIgnoreCase(partialName);
        } else {
            companies = transportCompanyRepository.findAll();
        }

        if ("name".equalsIgnoreCase(sortBy)) {
            if ("asc".equalsIgnoreCase(order)) {
                companies = companies.stream()
                        .sorted(Comparator.comparing(TransportCompany::getName))
                        .toList();
            } else if ("desc".equalsIgnoreCase(order)) {
                companies = companies.stream()
                        .sorted(Comparator.comparing(TransportCompany::getName).reversed())
                        .toList();
            }
        } else if ("revenue".equalsIgnoreCase(sortBy)) {
            if ("asc".equalsIgnoreCase(order)) {
                companies = companies.stream()
                        .sorted(Comparator.comparing(TransportCompany::getRevenue))
                        .toList();
            } else if ("desc".equalsIgnoreCase(order)) {
                companies = companies.stream()
                        .sorted(Comparator.comparing(TransportCompany::getRevenue).reversed())
                        .toList();
            }
        }

        return companies.stream()
                .map(transportCompanyMapper::toDto)
                .toList();
    }
}