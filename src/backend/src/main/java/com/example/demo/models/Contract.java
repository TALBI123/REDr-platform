package com.example.demo.models;

import com.example.demo.models.enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contracts")
@Filter(name = "agencyFilter", condition = "reservation_id in (select r.id from reservations r join cars c on r.car_id = c.id where c.agency_id = :agencyId)")
public class Contract extends BaseUuidEntity {

    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ContractStatus status = ContractStatus.ACTIVE;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "deposit_amount", precision = 19, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "terms", length = 4000)
    private String terms;

    @Column(name = "signed_by_client")
    private Boolean signedByClient;

    @Column(name = "signed_by_agency")
    private Boolean signedByAgency;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @JsonIgnoreProperties({"contract", "execution", "payments", "conditionReports"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", unique = true)
    private Reservation reservation;
}