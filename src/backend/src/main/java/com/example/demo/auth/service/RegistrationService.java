package com.example.demo.auth.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.dto.RegisterAgencyManagerRequest;
import com.example.demo.verification.service.EmailVerificationService;
import com.example.demo.models.Client;
import com.example.demo.models.AgencyManager;
import com.example.demo.models.Agency;
import com.example.demo.models.enums.AgencyStatus;
import com.example.demo.models.enums.UserRole;
import com.example.demo.models.enums.UserStatus;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.agency.repository.AgencyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final AgencyRepository agencyRepository;

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

    @Transactional
    public AgencyManager registerAgencyManager(RegisterAgencyManagerRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                "Email already registered: " + request.getEmail()
            );
        }

        Agency agency = new Agency();
        agency.setName(request.getAgencyName());
        agency.setCity(request.getAgencyCity());
        agency.setPhone(request.getAgencyPhone());
        agency.setEmail(request.getAgencyEmail());
        agency.setAddress(request.getAgencyAddress());
        agency.setDescription(request.getAgencyDescription());
        agency.setStatus(AgencyStatus.PENDING);
        Agency savedAgency = agencyRepository.save(agency);

        AgencyManager manager = new AgencyManager();
        manager.setEmail(request.getEmail());
        manager.setPassword(passwordEncoder.encode(request.getPassword()));
        manager.setFirstName(request.getFirstName());
        manager.setLastName(request.getLastName());
        manager.setPhone(request.getPhone());
        manager.setRole(UserRole.AGENCY_MANAGER);
        manager.setAccountStatus(UserStatus.PENDING);
        manager.setLicenceNumber(request.getLicenceNumber());
        manager.setNationalId(request.getNationalId());
        manager.setAgency(savedAgency);
        manager.setInscriptionDate(LocalDate.now());
        manager.setPasswordUpdatedAt(LocalDateTime.now());

        AgencyManager savedManager =
          (AgencyManager) userRepository.save(manager);
        userRepository.flush();

        emailVerificationService.sendVerificationEmail(savedManager.getEmail());

        return savedManager;
    }
}
