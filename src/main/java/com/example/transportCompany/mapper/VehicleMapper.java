package com.example.transportCompany.mapper;

import com.example.transportCompany.dto.VehicleDto;
import com.example.transportCompany.dto.VehicleResponseDto;
import com.example.transportCompany.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "typeId", source = "vehicleType.id")
    VehicleDto toDto(Vehicle vehicle);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "vehicleType", ignore = true)
    Vehicle toEntity(VehicleDto dto);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "typeName", source = "vehicleType.name")
    VehicleResponseDto toResponseDto(Vehicle vehicle);
}