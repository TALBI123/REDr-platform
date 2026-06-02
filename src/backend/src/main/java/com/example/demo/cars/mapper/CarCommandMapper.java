package com.example.demo.cars.mapper;

import com.example.demo.cars.dto.CarRequestDTO;
import com.example.demo.cars.dto.CarUpdateDTO;
import com.example.demo.models.agency.Car;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarCommandMapper {

    @Mapping(target = "agency", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "conditionReports", ignore = true)
    Car toEntity(CarRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "agency", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "conditionReports", ignore = true)
    void updateEntity(CarUpdateDTO dto, @MappingTarget Car car);
}
