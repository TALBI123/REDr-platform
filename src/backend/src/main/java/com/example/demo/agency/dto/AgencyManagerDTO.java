package com.example.demo.agency.dto;

import com.example.demo.models.enums.AgencyStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyManagerDTO {
    private String id;
    private String name;
    private String city;
    private String phone;
    private String email;
    private String address;
    private String description;
    private String logoUrl;
    private String iban;
    private Float rating;
    private AgencyStatus status;
    private LocalDate approvalDate;
    private LocalDate inscriptionDate;
    private String suspensionReason;
}