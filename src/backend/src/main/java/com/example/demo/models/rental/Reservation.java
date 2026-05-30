package com.example.demo.models.rental;

import com.example.demo.models.BaseUuidEntity;
import com.example.demo.models.agency.Car;
import com.example.demo.models.enums.ReservationStatus;
import com.example.demo.models.financial.Payment;
import com.example.demo.models.user.Client;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
@Filter(name = "agencyFilter", condition = "car_id in (select id from cars where agency_id = :agencyId)")
public class Reservation extends BaseUuidEntity {

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "deposit_amount", precision = 19, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "return_location")
    private String returnLocation;

    @Column(name = "late_return_fee", precision = 19, scale = 2)
    private BigDecimal lateReturnFee;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "actual_return_time")
    private LocalDateTime actualReturnTime;

    @JsonIgnoreProperties({"reservations", "payments"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @JsonIgnoreProperties({"reservations", "conditionReports", "photos"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<ConditionReport> conditionReports = new ArrayList<>();

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
    private Contract contract;

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
    private Execution execution;
}
