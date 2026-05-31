package com.example.demo.agency.mapper;

import com.example.demo.agency.dto.AgencyAdminDTO;
import com.example.demo.models.agency.Agency;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgencyAdminMapper {
	AgencyAdminDTO toDto(Agency agency);

	List<AgencyAdminDTO> toDtos(List<Agency> agencies);
}