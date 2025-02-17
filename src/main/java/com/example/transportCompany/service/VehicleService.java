package com.example.transportCompany.service;

import com.example.transportCompany.dto.VehicleDto;
import com.example.transportCompany.dto.VehicleResponseDto;

import java.util.List;

public interface VehicleService {
    VehicleResponseDto createVehicle(VehicleDto dto);
    VehicleResponseDto updateVehicle(Integer id, VehicleDto dto);
    VehicleResponseDto getVehicleById(Integer id);
    List<VehicleResponseDto> getAllVehicles();
    void deleteVehicle(Integer id);
}