package com.example.demo.user.controller;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.models.user.AppUser;
import com.example.demo.user.dto.AdminUserDTO;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserDTO>> listUsers(Pageable pageable) {
        Page<AdminUserDTO> users = userRepository.findAll(pageable).map(this::toDto);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable String userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return ResponseEntity.ok(toDto(user));
    }

    private AdminUserDTO toDto(AppUser user) {
        return new AdminUserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getAccountStatus(),
                user.getInscriptionDate(),
                user.getEmailVerifiedAt());
    }
}
