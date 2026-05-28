package com.example.demo.auth.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.verification.service.EmailVerificationService;
import com.example.demo.user.entity.Client;
import com.example.demo.user.enums.UserRole;
import com.example.demo.user.enums.UserStatus;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public Client register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        Client client = new Client();
        client.setEmail(request.getEmail());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhone(request.getPhone());
        client.setRole(UserRole.CLIENT);
        client.setAccountStatus(UserStatus.PENDING);
        client.setLicenceNumber(request.getLicenceNumber());
        client.setPassportNumber(request.getPassportNumber());
        client.setLocation(request.getLocation());
        client.setInscriptionDate(LocalDate.now());
        client.setPasswordUpdatedAt(LocalDateTime.now());

        Client savedClient = userRepository.save(client);
        userRepository.flush(); // force commit before email verification
        emailVerificationService.sendVerificationEmail(savedClient.getEmail());

        return savedClient;
    }
}
