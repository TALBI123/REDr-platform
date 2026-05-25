package com.example.demo.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.Security.JwtService;
import com.example.demo.entites.LoginRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public String login(LoginRequest loginDataRequest) {
        if (loginDataRequest == null) {
            throw new IllegalArgumentException("Login payload is required");
        }

        if (!StringUtils.hasText(loginDataRequest.getUsername())) {
            throw new IllegalArgumentException("Username is required");
        }

        if (!StringUtils.hasText(loginDataRequest.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDataRequest.getUsername(),
                loginDataRequest.getPassword()
            )
        );

        UserDetails user =
            userDetailsService.loadUserByUsername(
                loginDataRequest.getUsername()
            );

        return jwtService.generateAccessToken(user);
    }
}
