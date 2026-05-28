package com.example.demo.agency.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<String> list() {
        return ResponseEntity.ok("All agencies — visible to all authenticated users");
    }

    @GetMapping("/{agencyId}")
    @PreAuthorize("hasAnyRole('ADMIN','AGENCY_MANAGER')")
    public ResponseEntity<String> getOne(@PathVariable UUID agencyId) {
        securityUtils.assertAgencyAccess(agencyId);
        return ResponseEntity.ok("Agency " + agencyId);
    }
}
