package com.example.demo.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.AuthService;
import com.example.demo.entites.AuthResponse;
import com.example.demo.entites.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginDataRequest,
            HttpServletRequest request
    ) {
        String accessToken = authService.login(loginDataRequest);

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
            .httpOnly(true)
            .secure(request.isSecure())
            .path("/")
            .sameSite("Lax")
            .maxAge(Duration.ofDays(30))
            .build();

        AuthResponse response = new AuthResponse(
            "Login successful",
            loginDataRequest.getUsername()
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(response);
    }
}
