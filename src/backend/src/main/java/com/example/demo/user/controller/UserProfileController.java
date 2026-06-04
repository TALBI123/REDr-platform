package com.example.demo.user.controller;

import com.example.demo.user.dto.UserProfileDTO;
import com.example.demo.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

	private final ProfileService profileService;

	@GetMapping("/user")
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<UserProfileDTO> getUserProfile() {
		return ResponseEntity.ok(profileService.getCurrentUserProfile());
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<UserProfileDTO> getAdminProfile() {
		return ResponseEntity.ok(profileService.getCurrentAdminProfile());
	}

	@GetMapping("/agency-admin")
	@PreAuthorize("hasRole('AGENCY_MANAGER')")
	public ResponseEntity<UserProfileDTO> getAgencyAdminProfile() {
		return ResponseEntity.ok(profileService.getCurrentAgencyAdminProfile());
	}
}
