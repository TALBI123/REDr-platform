package com.example.demo.agency.mapper;

import com.example.demo.agency.dto.AgencyCreateUpdateDTO;
import com.example.demo.models.agency.Agency;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgencyCommandMapper {
    Agency toEntity(AgencyCreateUpdateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(AgencyCreateUpdateDTO dto, @MappingTarget Agency agency);
}
