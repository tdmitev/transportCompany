package com.example.transportCompany.service;

import com.example.transportCompany.dto.ClientDto;

import java.util.List;

public interface ClientService {
    ClientDto createClient(ClientDto dto);
    ClientDto getClientById(Integer id);
    List<ClientDto> getAllClients();
    ClientDto updateClient(Integer id, ClientDto dto);
    void deleteClient(Integer id);
}