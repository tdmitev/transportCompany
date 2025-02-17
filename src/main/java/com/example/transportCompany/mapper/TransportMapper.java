package com.example.transportCompany.mapper;

import com.example.transportCompany.dto.DriverTransportSummaryDto;
import com.example.transportCompany.dto.TransportDto;
import com.example.transportCompany.dto.TransportResponseDto;
import com.example.transportCompany.model.Employee;
import com.example.transportCompany.model.Transport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransportMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleLicensePlate", source = "vehicle.licensePlate")
    @Mapping(target = "driverId", source = "driver.id")
    @Mapping(target = "driverName", source = "driver.name")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientName", source = "client.name")
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "statusName", source = "status.name")
    TransportResponseDto toResponseDto(Transport transport);

    @Mapping(target = "driverId", source = "driver.id")
    @Mapping(target = "driverName", source = "driver.name")
    @Mapping(target = "totalPrice", source = "totalPrice")
    DriverTransportSummaryDto toDriverTransportSummaryDto(Employee driver, BigDecimal totalPrice);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "status", ignore = true)
    Transport toEntity(TransportDto dto);
}