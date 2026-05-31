package com.example.demo.agency.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyCreateUpdateDTO {

    @NotBlank
    @Size(max = 120)
    private String name;

    private String city;

    @Size(max = 30)
    private String phone;

    @NotBlank
    @Email
    private String email;

    private String address;

    private String description;

    private String logoUrl;

    private String iban;
}
