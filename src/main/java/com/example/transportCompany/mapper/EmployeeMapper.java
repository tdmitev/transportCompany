package com.example.transportCompany.mapper;

import com.example.transportCompany.dto.EmployeeDto;
import com.example.transportCompany.dto.EmployeeResponseDto;
import com.example.transportCompany.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "qualificationIds", ignore = true)
    @Mapping(target = "qualifications", ignore = true)
    @Mapping(target = "companyId", source = "company.id")
    EmployeeDto toDto(Employee employee);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "qualifications", ignore = true)
    Employee toEntity(EmployeeDto dto);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "qualifications", source = "qualifications")
    EmployeeResponseDto toResponseDto(Employee employee);
}