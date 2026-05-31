package com.example.demo.agency.controller;

import com.example.demo.agency.dto.AgencyAdminDTO;
import com.example.demo.agency.dto.AgencyCreateUpdateDTO;
import com.example.demo.agency.dto.AgencySearchCriteria;
import com.example.demo.agency.dto.ApproveAgencyRequest;
import com.example.demo.agency.dto.RejectAgencyRequest;
import com.example.demo.agency.mapper.AgencyAdminMapper;
import com.example.demo.agency.service.AgencyService;
import com.example.demo.models.agency.Agency;
import com.example.demo.user.service.AgencyAdminService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/agencies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AgencyAdminController {

	private final AgencyService agencyService;
	private final AgencyAdminService agencyAdminService;
	private final AgencyAdminMapper agencyAdminMapper;

	@GetMapping
	public ResponseEntity<Page<AgencyAdminDTO>> listAgencies(
			AgencySearchCriteria criteria,
			Pageable pageable) {
		Page<AgencyAdminDTO> result = agencyService
				.searchAgencies(criteria, pageable)
				.map(agencyAdminMapper::toDto);
		return ResponseEntity.ok(result);
	}

	// @GetMapping("/pending")
	// public ResponseEntity<List<AgencyAdminDTO>> pendingAgencies() {
	// 	List<AgencyAdminDTO> result = agencyAdminMapper
	// 			.toDtos(agencyAdminService.getPendingAgencies());
	// 	return ResponseEntity.ok(result);
	// }

	@GetMapping("/{agencyId}")
	public ResponseEntity<AgencyAdminDTO> getAgency(@PathVariable String agencyId) {
		Agency agency = agencyService.getById(agencyId);
		return ResponseEntity.ok(agencyAdminMapper.toDto(agency));
	}

	@PostMapping
	public ResponseEntity<AgencyAdminDTO> createAgency(
			@Valid @RequestBody AgencyCreateUpdateDTO request) {
		Agency agency = agencyService.createAgency(request);
		return ResponseEntity.status(201).body(agencyAdminMapper.toDto(agency));
	}

	@PutMapping("/{agencyId}")
	public ResponseEntity<AgencyAdminDTO> updateAgency(
			@PathVariable String agencyId,
			@Valid @RequestBody AgencyCreateUpdateDTO request) {
		Agency agency = agencyService.updateAgency(agencyId, request);
		return ResponseEntity.ok(agencyAdminMapper.toDto(agency));
	}

	@DeleteMapping("/{agencyId}")
	public ResponseEntity<Void> deleteAgency(@PathVariable String agencyId) {
		agencyService.deleteAgency(agencyId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{agencyId}/approve")
	public ResponseEntity<Void> approveAgency(
			@PathVariable String agencyId,
			@RequestBody ApproveAgencyRequest request) {
		agencyAdminService.approveAgency(agencyId, request.getComment());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{agencyId}/reject")
	public ResponseEntity<Void> rejectAgency(
			@PathVariable String agencyId,
			@Valid @RequestBody RejectAgencyRequest request) {
		agencyAdminService.rejectAgency(agencyId, request.getReason());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{agencyId}/suspend")
	public ResponseEntity<Void> suspendAgency(
			@PathVariable String agencyId,
			@Valid @RequestBody RejectAgencyRequest request) {
		agencyAdminService.suspendAgency(agencyId, request.getReason());
		return ResponseEntity.noContent().build();
	}
}