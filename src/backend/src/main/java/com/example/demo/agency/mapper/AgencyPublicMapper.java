package com.example.demo.agency.mapper;

import com.example.demo.agency.dto.AgencyPublicDTO;
import com.example.demo.models.agency.Agency;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgencyPublicMapper {
	AgencyPublicDTO toDto(Agency agency);

	List<AgencyPublicDTO> toDtos(List<Agency> agencies);
}
