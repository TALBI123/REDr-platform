package com.example.demo.booking.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateRequestDTO {

    private LocalDate startDate;

    private LocalDate endDate;

    private String pickupLocation;

    private String returnLocation;
}