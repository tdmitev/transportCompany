package com.example.transportCompany.service;

import com.example.transportCompany.dto.ClientDto;
import com.example.transportCompany.exception.ResourceNotFoundException;
import com.example.transportCompany.mapper.ClientMapper;
import com.example.transportCompany.model.Client;
import com.example.transportCompany.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientDto createClient(ClientDto dto) {
        Client client = clientMapper.toEntity(dto);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    public ClientDto getClientById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
        return clientMapper.toDto(client);
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }

    public ClientDto updateClient(Integer id, ClientDto dto) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
        
        existingClient.setName(dto.name());
        existingClient.setAddress(dto.address());
        existingClient.setEmail(dto.email());
        existingClient.setPhone(dto.phone());

        existingClient = clientRepository.save(existingClient);
        return clientMapper.toDto(existingClient);
    }

    public void deleteClient(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
        clientRepository.delete(client);
    }
}