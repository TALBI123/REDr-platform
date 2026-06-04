package com.example.demo.booking.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequestDTO {

    @NotNull
    private String carId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String pickupLocation;

    private String returnLocation;
}