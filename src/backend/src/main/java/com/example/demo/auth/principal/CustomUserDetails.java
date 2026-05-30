package com.example.demo.auth.principal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.models.user.AgencyManager;
import com.example.demo.models.user.AppUser;
import com.example.demo.models.enums.AgencyStatus;
import com.example.demo.models.enums.UserRole;
import com.example.demo.models.enums.UserStatus;

public class CustomUserDetails implements UserDetails {

    private final AppUser user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(AppUser user) {
        this.user = user;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    public static CustomUserDetails from(AppUser user) {
        return new CustomUserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (user.getAccountStatus() == UserStatus.LOCKED) {
            return false;
        }

        LocalDateTime lockUntil = user.getLockUntil();
        if (lockUntil != null && lockUntil.isAfter(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (user.getAccountStatus() != UserStatus.ACTIVE) {
            return false;
        }

        if (user instanceof AgencyManager manager) {
            AgencyStatus status = manager.getAgency() != null ? manager.getAgency().getStatus() : null;
            if (status != null && status != AgencyStatus.APPROVED) {
                return false;
            }
        }

        return true;
    }

    public AppUser getUser() {
        return user;
    }

    public String getUserId() {
        return user.getId();
    }

    public UserRole getRole() {
        return user.getRole();
    }

    public UserStatus getAccountStatus() {
        return user.getAccountStatus();
    }

    public String getAgencyId() {
        if (user instanceof AgencyManager manager && manager.getAgency() != null) {
            return manager.getAgency().getId();
        }

        return null;
    }
}
