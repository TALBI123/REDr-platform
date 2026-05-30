package com.example.demo.agency.controller;


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
@RequestMapping("/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private final SecurityUtils securityUtils;
    private final AgencyService agencyService;
    
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Agency>> getAgencies() {

        return ResponseEntity.ok(agencyService.getAllAgencies());
    }

    @GetMapping("/{agencyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','AGENCY_MANAGER')")
    public ResponseEntity<String> getOne(@PathVariable String agencyId) {
        securityUtils.assertAgencyAccess(agencyId);
        return ResponseEntity.ok("Agency " + agencyId);
    }
}
