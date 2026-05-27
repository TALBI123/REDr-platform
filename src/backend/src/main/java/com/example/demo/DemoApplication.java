package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.agency.entity.Agency;
import com.example.demo.agency.entity.AgencyStatus;
import com.example.demo.agency.repository.AgencyRepository;
import com.example.demo.user.entity.Admin;
import com.example.demo.user.entity.AgencyManager;
import com.example.demo.user.entity.Client;
import com.example.demo.user.enums.UserRole;
import com.example.demo.user.enums.UserStatus;
import com.example.demo.user.repository.UserRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
    CommandLineRunner start(
            UserRepository userRepository,
            AgencyRepository agencyRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Agency devAgency = findOrCreateDevAgency(agencyRepository);

            createAdminIfMissing(userRepository, passwordEncoder);
            createManagerIfMissing(userRepository, passwordEncoder, devAgency);
            createClientIfMissing(userRepository, passwordEncoder);
        };
    }

    private Agency findOrCreateDevAgency(AgencyRepository agencyRepository) {
        Optional<Agency> existingAgency = agencyRepository.findAll().stream()
                .filter(agency -> "Dev Agency".equals(agency.getName()))
                .findFirst();

        if (existingAgency.isPresent()) {
            return existingAgency.get();
        }

        Agency agency = new Agency();
        agency.setName("Dev Agency");
        agency.setStatus(AgencyStatus.APPROVED);
        return agencyRepository.save(agency);
    }

    private void createAdminIfMissing(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository.existsByEmail("admin@demo.com")) {
            return;
        }

        Admin admin = new Admin();
        admin.setEmail("admin@demo.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Demo");
        admin.setLastName("Admin");
        admin.setRole(UserRole.ADMIN);
        admin.setAccountStatus(UserStatus.ACTIVE);
        admin.setInscriptionDate(LocalDate.now());
        admin.setPasswordUpdatedAt(LocalDateTime.now());
        admin.setPermissions("ALL");
        admin.setAdminLevel(1);
        userRepository.save(admin);
    }

    private void createManagerIfMissing(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            Agency devAgency
    ) {
        if (userRepository.existsByEmail("manager@demo.com")) {
            return;
        }

        AgencyManager manager = new AgencyManager();
        manager.setEmail("manager@demo.com");
        manager.setPassword(passwordEncoder.encode("manager123"));
        manager.setFirstName("Demo");
        manager.setLastName("Manager");
        manager.setRole(UserRole.AGENCY_MANAGER);
        manager.setAccountStatus(UserStatus.ACTIVE);
        manager.setInscriptionDate(LocalDate.now());
        manager.setPasswordUpdatedAt(LocalDateTime.now());
        manager.setAgency(devAgency);
        manager.setLicenceNumber("MANAGER-LICENCE-001");
        manager.setNationalId("NATIONAL-ID-MANAGER-001");
        manager.setDigitalSignature("demo-manager-signature");
        manager.setResponsabilityLevel(1);
        userRepository.save(manager);
    }

    private void createClientIfMissing(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository.existsByEmail("client@demo.com")) {
            return;
        }

        Client client = new Client();
        client.setEmail("client@demo.com");
        client.setPassword(passwordEncoder.encode("client123"));
        client.setFirstName("Demo");
        client.setLastName("Client");
        client.setRole(UserRole.CLIENT);
        client.setAccountStatus(UserStatus.ACTIVE);
        client.setInscriptionDate(LocalDate.now());
        client.setPasswordUpdatedAt(LocalDateTime.now());
        client.setLicenceNumber("CLIENT-LICENCE-001");
        client.setLocation("Demo City");
        client.setLicenceExpirationDate(LocalDate.now().plusYears(1));
        client.setPassportNumber("PASSPORT-CLIENT-001");
        client.setDigitalSignature("demo-client-signature");
        userRepository.save(client);
    }
}
