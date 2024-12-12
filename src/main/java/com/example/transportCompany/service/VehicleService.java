package com.example.transportCompany.service;

import com.example.transportCompany.dto.VehicleDto;
import com.example.transportCompany.dto.VehicleResponseDto;
import com.example.transportCompany.exception.ResourceNotFoundException;
import com.example.transportCompany.mapper.VehicleMapper;
import com.example.transportCompany.model.TransportCompany;
import com.example.transportCompany.model.Vehicle;
import com.example.transportCompany.model.VehicleType;
import com.example.transportCompany.repository.TransportCompanyRepository;
import com.example.transportCompany.repository.VehicleRepository;
import com.example.transportCompany.repository.VehicleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final TransportCompanyRepository companyRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository,
                          TransportCompanyRepository companyRepository,
                          VehicleTypeRepository vehicleTypeRepository,
                          VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.companyRepository = companyRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public VehicleResponseDto createVehicle(VehicleDto dto) {
        Vehicle vehicle = vehicleMapper.toEntity(dto);

        TransportCompany company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + dto.companyId()));
        vehicle.setCompany(company);

        VehicleType vehicleType = vehicleTypeRepository.findById(dto.typeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with id " + dto.typeId()));
        vehicle.setVehicleType(vehicleType);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDto(savedVehicle);
    }

    public VehicleResponseDto updateVehicle(Integer id, VehicleDto dto) {

        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));

        existingVehicle.setLicensePlate(dto.licensePlate());
        existingVehicle.setFuelConsumption(dto.fuelConsumption());

        if (dto.companyId() != null) {
            TransportCompany company = companyRepository.findById(dto.companyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + dto.companyId()));
            existingVehicle.setCompany(company);
        }

        if (dto.typeId() != null) {
            VehicleType vehicleType = vehicleTypeRepository.findById(dto.typeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with id " + dto.typeId()));
            existingVehicle.setVehicleType(vehicleType);
        }

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

        return vehicleMapper.toResponseDto(updatedVehicle);
    }

    public VehicleResponseDto getVehicleById(Integer id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));

        return vehicleMapper.toResponseDto(vehicle);
    }

    public List<VehicleResponseDto> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::toResponseDto)
                .toList();
    }

    public void deleteVehicle(Integer id) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));

        vehicleRepository.delete(existingVehicle);
    }
}
