package com.example.demo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAgencyManagerRequest {

    // Manager fields
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String firstName;
    private String lastName;
    private String phone;

    @NotBlank
    private String licenceNumber;

    @NotBlank
    private String nationalId;

    // Agency fields
    @NotBlank
    private String agencyName;

    private String agencyCity;
    private String agencyPhone;
    private String agencyEmail;
    private String agencyAddress;
    private String agencyDescription;
}
