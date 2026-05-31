package com.example.demo.agency.controller;

import com.example.demo.agency.dto.AgencyCreateUpdateDTO;
import com.example.demo.agency.dto.AgencyManagerDTO;
import com.example.demo.agency.mapper.AgencyManagerMapper;
import com.example.demo.agency.service.AgencyService;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.models.agency.Agency;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager/agencies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AGENCY_MANAGER')")
public class AgencyManagerController {

	private final AgencyService agencyService;
	private final AgencyManagerMapper agencyManagerMapper;
	private final SecurityUtils securityUtils;

	@GetMapping("/")
	public ResponseEntity<AgencyManagerDTO> getMyAgency() {
		String agencyId = securityUtils.getCurrentAgencyId();
		Agency agency = agencyService.getById(agencyId);
		return ResponseEntity.ok(agencyManagerMapper.toDto(agency));
	}

	@PutMapping("/")
	public ResponseEntity<AgencyManagerDTO> updateMyAgency(
			@Valid @RequestBody AgencyCreateUpdateDTO request) {
		String agencyId = securityUtils.getCurrentAgencyId();
		Agency agency = agencyService.updateAgency(agencyId, request);
		return ResponseEntity.ok(agencyManagerMapper.toDto(agency));
	}
}