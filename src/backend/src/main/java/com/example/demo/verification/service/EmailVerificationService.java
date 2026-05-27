package com.example.demo.verification.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.user.entity.AppUser;
import com.example.demo.user.enums.UserStatus;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.verification.entity.EmailVerificationToken;
import com.example.demo.verification.repository.EmailVerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.verification.token-expiry-hours}")
    private int tokenExpiryHours;

    public void sendVerificationEmail(String email) {
        tokenRepository.deleteByEmail(email);

        String rawToken = UUID.randomUUID().toString();

        EmailVerificationToken entity = new EmailVerificationToken();
        entity.setToken(rawToken);
        entity.setEmail(email);
        entity.setExpiresAt(LocalDateTime.now().plusHours(tokenExpiryHours));
        tokenRepository.save(entity);

        emailService.sendVerificationEmail(email, rawToken);
    }

    public void verifyEmail(String rawToken) {
        EmailVerificationToken record = tokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new ResourceNotFoundException("Verification token not found"));

        if (record.isUsed()) {
            throw new IllegalArgumentException("Verification token already used");
        }

        if (record.isExpired()) {
            throw new IllegalArgumentException("Verification token has expired");
        }

        record.setUsedAt(LocalDateTime.now());
        tokenRepository.save(record);

        AppUser user = userRepository.findByEmailAndDeletedAtIsNull(record.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setAccountStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
