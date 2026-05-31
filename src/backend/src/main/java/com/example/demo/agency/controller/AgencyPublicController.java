package com.example.demo.agency.controller;

import com.example.demo.agency.dto.AgencyPublicDTO;
import com.example.demo.agency.dto.AgencySearchCriteria;
import com.example.demo.agency.mapper.AgencyPublicMapper;
import com.example.demo.agency.service.AgencyService;
import com.example.demo.models.agency.Agency;
import com.example.demo.models.enums.AgencyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agencies")
@RequiredArgsConstructor
public class AgencyPublicController {

	private final AgencyService agencyService;
	private final AgencyPublicMapper agencyPublicMapper;

	@GetMapping
	public ResponseEntity<Page<AgencyPublicDTO>> listPublicAgencies(
			AgencySearchCriteria criteria,
			Pageable pageable) {
		criteria.setStatus(AgencyStatus.APPROVED);
		Page<AgencyPublicDTO> result = agencyService
				.searchAgencies(criteria, pageable)
				.map(agencyPublicMapper::toDto);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{agencyId}")
	public ResponseEntity<AgencyPublicDTO> getPublicAgency(
			@PathVariable String agencyId) {
		Agency agency = agencyService.getByIdAndStatus(agencyId, AgencyStatus.APPROVED);
		return ResponseEntity.ok(agencyPublicMapper.toDto(agency));
	}
}