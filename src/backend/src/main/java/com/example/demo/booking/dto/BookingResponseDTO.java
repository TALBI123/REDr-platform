package com.example.demo.booking.dto;

import com.example.demo.models.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private String id;
    private ReservationStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime creationDate;
    private LocalDateTime completedAt;
    private BigDecimal totalAmount;
    private String pickupLocation;
    private String returnLocation;
    private String cancellationReason;
    private String clientId;
    private String carId;
    private String carBrand;
    private String carMode;
    private String agencyId;
    private String agencyName;
}