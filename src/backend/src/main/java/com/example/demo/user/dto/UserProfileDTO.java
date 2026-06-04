package com.example.demo.user.dto;

import com.example.demo.models.enums.UserRole;
import com.example.demo.models.enums.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

	private String id;
	private UserRole role;
	private UserStatus accountStatus;

	private String email;
	private String firstName;
	private String lastName;
	private LocalDate inscriptionDate;
	private LocalDateTime emailVerifiedAt;

	// Client / Agency manager common details
	private String phone;

	// Client details
	private String licenceNumber;
	private String nationalId;
	private String passportNumber;
	private String location;

	// Super admin details
	private Integer adminLevel;
	private String permissions;

	// Agency manager details
	private String agencyId;
	private String agencyName;
	private LocalDateTime approvedAt;
}
