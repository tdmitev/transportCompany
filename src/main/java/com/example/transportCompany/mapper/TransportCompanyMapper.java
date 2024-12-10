package com.example.transportCompany.mapper;

import com.example.transportCompany.dto.TransportCompanyDto;
import com.example.transportCompany.model.TransportCompany;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransportCompanyMapper {

    TransportCompanyDto toDto(TransportCompany company);

    TransportCompany toEntity(TransportCompanyDto dto);
}