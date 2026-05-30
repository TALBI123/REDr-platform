package com.example.demo.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.agency.Agency;
import com.example.demo.models.enums.AgencyStatus;
import com.example.demo.agency.repository.AgencyRepository;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.models.user.AgencyManager;
import com.example.demo.models.enums.UserRole;
import com.example.demo.models.enums.UserStatus;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AgencyAdminService {

    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public List<Agency> getPendingAgencies() {
        return agencyRepository.findByStatus(AgencyStatus.PENDING);
    }

    public void approveAgency(String agencyId, String comment) {

        Agency agency = agencyRepository.findById(agencyId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Agency not found: " + agencyId));

        if (agency.getStatus() != AgencyStatus.PENDING) {
            throw new IllegalArgumentException(
                "Agency is not in PENDING status");
        }

        agency.setStatus(AgencyStatus.APPROVED);
        agency.setApprovalDate(LocalDate.now());
        agencyRepository.save(agency);

        String adminId = securityUtils.getCurrentUserId();

        userRepository.findByRole(UserRole.AGENCY_MANAGER).stream()
            .filter(u -> u instanceof AgencyManager)
            .map(u -> (AgencyManager) u)
            .filter(m -> m.getAgency() != null
                && agencyId.equals(m.getAgency().getId()))
            .filter(m -> m.getEmailVerifiedAt() != null)
            .forEach(m -> {
                m.setAccountStatus(UserStatus.ACTIVE);
                m.setApprovedAt(LocalDateTime.now());
                m.setApprovedByAdminId(adminId);
                userRepository.save(m);
                // TODO: send approval notification email to manager
            });
    }

    public void rejectAgency(String agencyId, String reason) {

        Agency agency = agencyRepository.findById(agencyId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Agency not found: " + agencyId));

        if (agency.getStatus() != AgencyStatus.PENDING) {
            throw new IllegalArgumentException(
                "Agency is not in PENDING status");
        }

        agency.setStatus(AgencyStatus.REJECTED);
        agency.setSuspensionReason(reason);
        agencyRepository.save(agency);

        userRepository.findByRole(UserRole.AGENCY_MANAGER).stream()
            .filter(u -> u instanceof AgencyManager)
            .map(u -> (AgencyManager) u)
            .filter(m -> m.getAgency() != null
                && agencyId.equals(m.getAgency().getId()))
            .forEach(m -> {
                m.setAccountStatus(UserStatus.SUSPENDED);
                m.setRejectionReason(reason);
                userRepository.save(m);
                // TODO: send rejection notification email to manager
            });
    }

    public void suspendAgency(String agencyId, String reason) {

        Agency agency = agencyRepository.findById(agencyId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Agency not found: " + agencyId));

        if (agency.getStatus() != AgencyStatus.APPROVED) {
            throw new IllegalArgumentException(
                "Only APPROVED agencies can be suspended");
        }

        agency.setStatus(AgencyStatus.SUSPENDED);
        agency.setSuspensionReason(reason);
        agencyRepository.save(agency);

        // Do NOT touch manager accountStatus here.
        // CustomUserDetails.isEnabled() already checks agencyStatus on every
        // request. The manager's next API call will return 401 automatically
        // because the agency is now SUSPENDED. No token invalidation needed.
    }
}
