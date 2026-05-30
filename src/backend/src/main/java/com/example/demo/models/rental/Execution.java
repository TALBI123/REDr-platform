package com.example.demo.models.rental;

import com.example.demo.models.enums.ExecutionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "executions")
public class Execution {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actual_return_time")
    private LocalDateTime actualReturnTime;

    @Column(name = "actual_pickup_date")
    private LocalDateTime actualPickupDate;

    @Column(name = "delay_in_days")
    private Integer delayInDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExecutionStatus status = ExecutionStatus.ONGOING;

    @JsonIgnoreProperties({"contract", "execution", "payments", "conditionReports"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", unique = true)
    private Reservation reservation;
}
