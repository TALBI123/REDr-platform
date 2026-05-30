package com.example.demo.models.agency;

import com.example.demo.models.BaseUuidEntity;
import com.example.demo.models.enums.ConditionStatus;
import com.example.demo.models.enums.FuelType;
import com.example.demo.models.enums.TransmissionType;
import com.example.demo.models.enums.VehicleStatus;
import com.example.demo.models.rental.ConditionReport;
import com.example.demo.models.rental.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cars")
@FilterDef(name = "agencyFilter", parameters = @ParamDef(name = "agencyId", type = String.class))
@Filter(name = "agencyFilter", condition = "agency_id = :agencyId")
public class Car extends BaseUuidEntity {

    @Column(name = "mode")
    private String mode;

    @Column(name = "brand")
    private String brand;

    @Column(name = "manufacture_year")
    private Integer year;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "car_fuel_types", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    private List<FuelType> fuelTypes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission_type")
    private TransmissionType transmissionType;

    @Column(name = "mileage")
    private Integer mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private VehicleStatus currentStatus = VehicleStatus.Available;

    @Column(name = "daily_price", precision = 19, scale = 2)
    private BigDecimal dailyPrice;

    @Column(name = "weekly_price", precision = 19, scale = 2)
    private BigDecimal weeklyPrice;

    @Column(name = "monthly_price", precision = 19, scale = 2)
    private BigDecimal monthlyPrice;

    @Column(name = "promotion_active")
    private Boolean promotionActive;

    @Column(name = "promotion_rate", precision = 5, scale = 2)
    private BigDecimal promotionRate;

    @Column(name = "promotion_start_date")
    private LocalDate promotionStartDate;

    @Column(name = "promotion_end_date")
    private LocalDate promotionEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_status")
    private ConditionStatus conditionStatus;

    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;

    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "insurance_policy_number")
    private String insurancePolicyNumber;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "seat_capacity")
    private Integer seatCapacity;

    @Column(name = "technical_notes", length = 4000)
    private String technicalNotes;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "agencies_notified")
    private Boolean agenciesNotified;

    @Column(name = "notification_sent_at")
    private LocalDateTime notificationSentAt;

    @Column(name = "notification_days_before")
    private Integer notificationDaysBefore;

    @JsonIgnoreProperties({"cars", "managers"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @JsonIgnoreProperties({"cars"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<ConditionReport> conditionReports = new ArrayList<>();
}
