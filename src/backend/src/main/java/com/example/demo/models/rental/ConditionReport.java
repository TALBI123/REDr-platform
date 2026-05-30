package com.example.demo.models.rental;

import com.example.demo.models.agency.Car;
import com.example.demo.models.agency.Photo;
import com.example.demo.models.enums.ConditionReportType;
import com.example.demo.models.enums.FuelLevel;
import com.example.demo.models.enums.VehicleStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "condition_reports")
public class ConditionReport {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "report_number")
    private String reportNumber;

    @Column(name = "date_environ")
    private LocalDateTime dateEnviron;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "comment", length = 4000)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ConditionReportType type;

    @Column(name = "is_signed_by_customer")
    private Boolean isSignedByCustomer;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_status_before")
    private VehicleStatus vehicleStatusBefore;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_status_after")
    private VehicleStatus vehicleStatusAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_level_before")
    private FuelLevel fuelLevelBefore;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_level_after")
    private FuelLevel fuelLevelAfter;

    @Column(name = "signature_url")
    private String signatureUrl;

    @JsonIgnoreProperties({"reservations", "conditionReports", "photos"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @JsonIgnoreProperties({"reservations", "conditionReports", "photos"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "conditionReport", fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();
}
