package com.example.demo.models.financial;

import com.example.demo.models.AuditableUuidEntity;
import com.example.demo.models.enums.PaymentStatus;
import com.example.demo.models.rental.Reservation;
import com.example.demo.models.user.Client;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends AuditableUuidEntity {

    @Column(name = "type")
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @JsonIgnoreProperties({"payments", "reservations"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @JsonIgnoreProperties({"payments", "reservations"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}
