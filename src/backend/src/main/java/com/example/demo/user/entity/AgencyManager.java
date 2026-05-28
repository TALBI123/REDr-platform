package com.example.demo.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.agency.entity.Agency;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agency_manager")
@DiscriminatorValue("AGENCY_MANAGER")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AgencyManager extends AppUser {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    private String licenceNumber;
    @jakarta.persistence.Column(name = "manager_national_id")
    private String nationalId;

    private String digitalSignature;

    private int responsabilityLevel;

    private LocalDateTime approvedAt;
    private UUID approvedByAdminId;
    private String rejectionReason;
}