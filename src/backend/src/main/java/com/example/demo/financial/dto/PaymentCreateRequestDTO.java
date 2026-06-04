package com.example.demo.financial.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequestDTO {

    @NotBlank
    private String reservationId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amount;

    private String type; // e.g. CARD, CASH
}
