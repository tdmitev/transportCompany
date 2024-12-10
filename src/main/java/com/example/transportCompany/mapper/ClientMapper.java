package com.example.transportCompany.mapper;

import com.example.transportCompany.dto.ClientDto;
import com.example.transportCompany.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toDto(Client client);

    Client toEntity(ClientDto clientDTO);
}