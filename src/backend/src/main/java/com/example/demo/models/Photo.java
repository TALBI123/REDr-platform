package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
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
@Table(name = "photos")
public class Photo extends AuditableUuidEntity {

    @Column(name = "url")
    private String url;

    @Column(name = "secure_url")
    private String secureUrl;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at_override")
    private LocalDateTime createdAtOverride;

    @JsonIgnoreProperties({"photos", "reservations", "conditionReports"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @JsonIgnoreProperties({"photos", "reservations", "conditionReports"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_report_id")
    private ConditionReport conditionReport;
}