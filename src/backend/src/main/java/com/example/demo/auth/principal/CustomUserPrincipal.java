package com.example.demo.auth.principal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.user.entity.AppUser;
import com.example.demo.user.entity.AgencyManager;
import com.example.demo.user.enums.UserRole;
import com.example.demo.user.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserPrincipal implements UserDetails {

    private final UUID userId;
    private final String email;
    private final String password;
    private final UserRole role;
    private final UserStatus accountStatus;
    private final UUID agencyId;
    private final LocalDateTime lockUntil;
    private final Collection<? extends GrantedAuthority> authorities;

    public static CustomUserPrincipal from(AppUser user) {
        UUID agencyId = null;

        if (user instanceof AgencyManager manager && manager.getAgency() != null) {
            agencyId = manager.getAgency().getId();
        }

        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getAccountStatus(),
                agencyId,
                user.getLockUntil(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (accountStatus == UserStatus.LOCKED) {
            return false;
        }

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
        return accountStatus == UserStatus.ACTIVE;
    }
}