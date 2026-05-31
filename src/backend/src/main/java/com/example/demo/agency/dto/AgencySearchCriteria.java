package com.example.demo.agency.dto;

import com.example.demo.models.enums.AgencyStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencySearchCriteria {

    private String name;
    private String city;
    private String email;
    private String phone;
    private AgencyStatus status;
    private Float minRating;
    private Float maxRating;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate approvalFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate approvalTo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inscriptionFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inscriptionTo;
}
