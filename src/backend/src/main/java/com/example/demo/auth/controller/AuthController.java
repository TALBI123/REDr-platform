package com.example.demo.auth.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.LoginResult;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.dto.RegisterAgencyManagerRequest;
import com.example.demo.auth.principal.CustomUserDetails;
import com.example.demo.auth.service.AuthService;
import com.example.demo.auth.service.RegistrationService;
import com.example.demo.models.user.Client;
import com.example.demo.models.user.AgencyManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;
        private final RegistrationService registrationService;

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(
                        @Valid @RequestBody LoginRequest loginRequest,
                        HttpServletRequest request) {
                LoginResult result = authService.login(loginRequest);
                String accessToken = result.token();
                CustomUserDetails principal = result.principal();

                ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                                .httpOnly(true)
                                .secure(request.isSecure())
                                .path("/")
                                .sameSite("Lax")
                                .maxAge(Duration.ofDays(30))
                                .build();

                AuthResponse response = new AuthResponse("Login successful", principal.getUsername(),
                                principal.getRole().name());

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(response);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(HttpServletRequest request) {
                ResponseCookie clear = ResponseCookie.from("ACCESS_TOKEN", "")
                                .httpOnly(true)
                                .secure(request.isSecure())
                                .path("/")
                                .sameSite("Lax")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, clear.toString())
                                .build();
        }

        @PostMapping("/register/client")
        public ResponseEntity<AuthResponse> register(
                        @Valid @RequestBody RegisterRequest request) {
                Client client = registrationService.register(request);
                AuthResponse body = new AuthResponse(
                                "Registration successful. Please verify your email.",
                                client.getEmail(),
                                client.getRole().name());

                return ResponseEntity.status(201).body(body);
        }

        @PostMapping("/register/agency")
        public ResponseEntity<AuthResponse> registerAgency(
                        @Valid @RequestBody RegisterAgencyManagerRequest request) {
                AgencyManager manager = registrationService.registerAgencyManager(request);

                AuthResponse body = new AuthResponse(
                                "Registration successful. Please verify your email." +
                                                " Your account will be reviewed by an administrator.",
                                manager.getEmail(),
                                manager.getRole().name());

                return ResponseEntity.status(201).body(body);
        }
}