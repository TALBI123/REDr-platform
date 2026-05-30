package com.example.demo.models.agency;

import com.example.demo.models.BaseUuidEntity;
import com.example.demo.models.enums.AgencyStatus;
import com.example.demo.models.user.AgencyManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agencies")
public class Agency extends BaseUuidEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "phone",unique=true)
    private String phone;

    // @Column(name = "location")
    // private String location;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AgencyStatus status = AgencyStatus.PENDING;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "inscription_date")
    private LocalDate inscriptionDate;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "iban")
    private String iban;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "suspension_reason")
    private String suspensionReason;

    @JsonIgnore
    @OneToMany(mappedBy = "agency", fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();

    @JsonIgnoreProperties({"agency"})
    @OneToMany(mappedBy = "agency", fetch = FetchType.LAZY)
    private List<AgencyManager> managers = new ArrayList<>();
}
