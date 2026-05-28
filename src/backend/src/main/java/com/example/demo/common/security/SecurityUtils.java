package com.example.demo.common.security;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.auth.principal.CustomUserPrincipal;
import com.example.demo.user.enums.UserRole;

@Component
public class SecurityUtils {

    public CustomUserPrincipal getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new AccessDeniedException("No authenticated user");
        }
        return principal;
    }

    public UUID getCurrentUserId() {
        return getCurrentPrincipal().getUserId();
    }

    public UUID getCurrentAgencyId() {
        UUID agencyId = getCurrentPrincipal().getAgencyId();
        if (agencyId == null) {
            throw new AccessDeniedException("User is not associated with any agency");
        }
        return agencyId;
    }

    public boolean isAdmin() {
        return getCurrentPrincipal().getRole() == UserRole.ADMIN;
    }

    public boolean isAgencyManager() {
        return getCurrentPrincipal().getRole() == UserRole.AGENCY_MANAGER;
    }

    public void assertAgencyAccess(UUID requestedAgencyId) {
        if (isAdmin()) {
            return;
        }
        if (!getCurrentAgencyId().equals(requestedAgencyId)) {
            throw new AccessDeniedException("Access denied: agency mismatch");
        }
    }
}
