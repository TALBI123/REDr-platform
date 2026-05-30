package com.example.demo.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.agency.entity.Agency;
import com.example.demo.agency.repository.AgencyRepository;
import com.example.demo.user.dto.ApproveAgencyRequest;
import com.example.demo.user.dto.RejectAgencyRequest;
import com.example.demo.user.service.AgencyAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AgencyAdminService agencyAdminService;
    private final AgencyRepository agencyRepository;

    @GetMapping("/users")
    public ResponseEntity<String> listUsers() {
        return ResponseEntity.ok("Admin: list all users");
    }

    @GetMapping("/agencies")
    public ResponseEntity<String> listAgencies() {
        return ResponseEntity.ok("Admin: list all agencies");
    }

    @GetMapping("/agencies/pending")
    public ResponseEntity<List<Agency>> pendingAgencies() {
        return ResponseEntity.ok(agencyAdminService.getPendingAgencies());
    }

    @PostMapping("/agencies/{agencyId}/approve")
    public ResponseEntity<Void> approveAgency(
            @PathVariable UUID agencyId,
            @RequestBody ApproveAgencyRequest request
    ) {
        agencyAdminService.approveAgency(agencyId, request.getComment());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/agencies/{agencyId}/reject")
    public ResponseEntity<Void> rejectAgency(
            @PathVariable UUID agencyId,
            @jakarta.validation.Valid @RequestBody RejectAgencyRequest request
    ) {
        agencyAdminService.rejectAgency(agencyId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/agencies/{agencyId}/suspend")
    public ResponseEntity<Void> suspendAgency(
            @PathVariable UUID agencyId,
            @jakarta.validation.Valid @RequestBody RejectAgencyRequest request
    ) {
        agencyAdminService.suspendAgency(agencyId, request.getReason());
        return ResponseEntity.ok().build();
    }
}
