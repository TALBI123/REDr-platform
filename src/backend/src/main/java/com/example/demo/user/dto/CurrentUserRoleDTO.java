package com.example.demo.user.dto;

import com.example.demo.models.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserRoleDTO {
    private String userId;
    private String email;
    private UserRole role;
    private String agencyId;
}
