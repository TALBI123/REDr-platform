package com.example.demo.agency.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectAgencyRequest {

    @NotBlank
    @Size(min = 20, message = "Reason must be at least 20 characters")
    private String reason;
}
