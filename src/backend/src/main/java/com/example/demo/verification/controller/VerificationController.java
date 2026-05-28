package com.example.demo.verification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.verification.dto.ResendVerificationRequest;
import com.example.demo.verification.service.EmailVerificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final EmailVerificationService verificationService;

    @GetMapping("/verify-email")
    public ResponseEntity<AuthResponse> verify(@RequestParam String token) {
        verificationService.verifyEmail(token);
        return ResponseEntity.ok(new AuthResponse("Email verified successfully. You can now log in.", null, null));
    }

    @PostMapping("/resend")
    public ResponseEntity<AuthResponse> resend(@RequestBody @Valid ResendVerificationRequest request) {
        verificationService.sendVerificationEmail(request.getEmail());
        return ResponseEntity.ok(new AuthResponse("Verification email sent.", request.getEmail(), null));
    }
}
