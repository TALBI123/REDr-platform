package com.example.demo.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.LoginResult;
import com.example.demo.auth.principal.CustomUserDetails;
import com.example.demo.models.user.AppUser;
import com.example.demo.models.enums.UserStatus;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public LoginResult login(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Login payload is required");
        }

        if (!StringUtils.hasText(request.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }

        AppUser user = userRepository
                .findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (user.getAccountStatus() == UserStatus.LOCKED) {
            if (user.getLockUntil() != null
                    && user.getLockUntil().isAfter(LocalDateTime.now())) {
                throw new LockedException("Account is locked. Try again later.");
            } else {
                user.setAccountStatus(UserStatus.ACTIVE);
                user.setFailedLoginAttempts(0);
                user.setLockUntil(null);
                userRepository.save(user);
            }
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= 5) {
                user.setAccountStatus(UserStatus.LOCKED);
                user.setLockUntil(LocalDateTime.now().plusMinutes(30));
                // TODO: send lock alert email via EmailService
            }

            userRepository.save(user);
            throw e;
        }

        user.setFailedLoginAttempts(0);
        user.setLockUntil(null);
        userRepository.save(user);

        CustomUserDetails principal = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateAccessToken(principal);
        return new LoginResult(token, principal);
    }
}