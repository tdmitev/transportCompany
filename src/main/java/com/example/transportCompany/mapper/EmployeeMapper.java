package com.example.transportCompany.mapper;

import com.example.transportCompany.dto.EmployeeDto;
import com.example.transportCompany.dto.EmployeeResponseDto;
import com.example.transportCompany.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "transports", ignore = true)
    @Mapping(target = "qualifications", ignore = true)
    Employee toEntity(EmployeeDto dto);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "qualifications", source = "qualifications")
    EmployeeResponseDto toResponseDto(Employee employee);
}