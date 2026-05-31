package com.example.demo.agency.mapper;

import com.example.demo.agency.dto.AgencyManagerDTO;
import com.example.demo.models.agency.Agency;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgencyManagerMapper {
	AgencyManagerDTO toDto(Agency agency);

	List<AgencyManagerDTO> toDtos(List<Agency> agencies);
}
