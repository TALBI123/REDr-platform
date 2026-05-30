package com.example.demo.models.user;

import com.example.demo.models.agency.Agency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("AGENCY_MANAGER")
@Table(name = "agency_managers")
public class AgencyManager extends AppUser {

    @Column(name = "phone")
    private String phone;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "digital_signature")
    private String digitalSignature;

    @Column(name = "responsability_level")
    private int responsabilityLevel;

    @Column(name = "licence_number")
    private String licenceNumber;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by_admin_id")
    private String approvedByAdminId;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @JsonIgnoreProperties({"cars", "managers"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;
}
