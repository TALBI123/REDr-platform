package com.example.demo.agency.controller;

import com.example.demo.agency.dto.AgencyAdminDTO;
import com.example.demo.agency.mapper.AgencyAdminMapper;
import com.example.demo.agency.service.AgencyService;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.models.agency.Agency;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/agencies")
@RequiredArgsConstructor
public class AgencyController {

	private final SecurityUtils securityUtils;
	private final AgencyService agencyService;
	private final AgencyAdminMapper agencyAdminMapper;
    
	@GetMapping
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<List<AgencyAdminDTO>> getAgencies() {
		List<Agency> agencies = agencyService.getAllAgencies();
		return ResponseEntity.ok(agencyAdminMapper.toDtos(agencies));
	}

	@GetMapping("/{agencyId}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN','AGENCY_MANAGER')")
	public ResponseEntity<AgencyAdminDTO> getOne(@PathVariable String agencyId) {
		securityUtils.assertAgencyAccess(agencyId);
		Agency agency = agencyService.getById(agencyId);
		return ResponseEntity.ok(agencyAdminMapper.toDto(agency));
	}
}

