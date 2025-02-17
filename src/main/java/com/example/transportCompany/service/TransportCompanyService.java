package com.example.transportCompany.service;

import com.example.transportCompany.dto.TransportCompanyDto;

import java.util.List;

public interface TransportCompanyService {
    TransportCompanyDto createCompany(TransportCompanyDto dto);
    TransportCompanyDto getCompanyById(Integer id);
    List<TransportCompanyDto> getAllCompanies();
    TransportCompanyDto updateCompany(Integer id, TransportCompanyDto dto);
    void deleteCompany(Integer id);

    List<TransportCompanyDto> getSortedAndFilteredCompanies(String sortBy, String order, String partialName);
}
