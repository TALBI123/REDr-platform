package com.example.demo.user.service;

import com.example.demo.auth.principal.CustomUserDetails;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.models.user.Admin;
import com.example.demo.models.user.AgencyManager;
import com.example.demo.models.user.AppUser;
import com.example.demo.models.user.Client;
import com.example.demo.user.dto.CurrentUserRoleDTO;
import com.example.demo.user.dto.UserProfileDTO;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

	private final UserRepository userRepository;
	private final SecurityUtils securityUtils;

	public UserProfileDTO getCurrentUserProfile() {
		AppUser user = getCurrentUser();
		if (!(user instanceof Client)) {
			throw new AccessDeniedException("Only clients can access this profile endpoint");
		}
		return toDto(user);
	}

	public UserProfileDTO getCurrentAdminProfile() {
		AppUser user = getCurrentUser();
		if (!(user instanceof Admin)) {
			throw new AccessDeniedException("Only super admins can access this profile endpoint");
		}
		return toDto(user);
	}

	public UserProfileDTO getCurrentAgencyAdminProfile() {
		AppUser user = getCurrentUser();
		if (!(user instanceof AgencyManager)) {
			throw new AccessDeniedException("Only agency managers can access this profile endpoint");
		}
		return toDto(user);
	}

	public CurrentUserRoleDTO getCurrentUserRole() {
		CustomUserDetails principal = securityUtils.getCurrentPrincipal();
		return new CurrentUserRoleDTO(
				principal.getUserId(),
				principal.getUsername(),
				principal.getRole(),
				principal.getAgencyId());
	}

	private AppUser getCurrentUser() {
		String userId = securityUtils.getCurrentUserId();
		return userRepository.findByIdAndDeletedAtIsNull(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
	}

	private UserProfileDTO toDto(AppUser user) {
		UserProfileDTO dto = new UserProfileDTO();
		dto.setId(user.getId());
		dto.setRole(user.getRole());
		dto.setAccountStatus(user.getAccountStatus());
		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setInscriptionDate(user.getInscriptionDate());
		dto.setEmailVerifiedAt(user.getEmailVerifiedAt());

		if (user instanceof Client client) {
			dto.setPhone(client.getPhone());
			dto.setLicenceNumber(client.getLicenceNumber());
			dto.setNationalId(client.getNationalId());
			dto.setPassportNumber(client.getPassportNumber());
			dto.setLocation(client.getLocation());
		}

		if (user instanceof Admin admin) {
			dto.setAdminLevel(admin.getAdminLevel());
			dto.setPermissions(admin.getPermissions());
		}

		if (user instanceof AgencyManager manager) {
			dto.setPhone(manager.getPhone());
			dto.setLicenceNumber(manager.getLicenceNumber());
			dto.setNationalId(manager.getNationalId());
			dto.setApprovedAt(manager.getApprovedAt());
			if (manager.getAgency() != null) {
				dto.setAgencyId(manager.getAgency().getId());
				dto.setAgencyName(manager.getAgency().getName());
			}
		}

		return dto;
	}
}
