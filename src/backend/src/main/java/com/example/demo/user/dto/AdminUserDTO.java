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
public class AdminUserDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus accountStatus;
    private LocalDate inscriptionDate;
    private LocalDateTime emailVerifiedAt;
}
