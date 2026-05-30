package com.example.demo.common.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.auth.principal.CustomUserDetails;
import com.example.demo.models.enums.UserRole;

@Component
public class SecurityUtils {

    public CustomUserDetails getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails principal)) {
            throw new AccessDeniedException("No authenticated user");
        }
        return principal;
    }

    public String getCurrentUserId() {
        return getCurrentPrincipal().getUserId();
    }

    public String getCurrentAgencyId() {
        String agencyId = getCurrentPrincipal().getAgencyId();
        if (agencyId == null) {
            throw new AccessDeniedException("User is not associated with any agency");
        }
        return agencyId;
    }

    public boolean isAdmin() {
        return getCurrentPrincipal().getRole() == UserRole.SUPER_ADMIN;
    }

    public boolean isAgencyManager() {
        return getCurrentPrincipal().getRole() == UserRole.AGENCY_MANAGER;
    }

    public void assertAgencyAccess(String requestedAgencyId) {
        if (isAdmin()) {
            return;
        }
        if (!getCurrentAgencyId().equals(requestedAgencyId)) {
            throw new AccessDeniedException("Access denied: agency mismatch");
        }
    }
}
