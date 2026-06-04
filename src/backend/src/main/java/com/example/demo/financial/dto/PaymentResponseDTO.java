package com.example.demo.financial.dto;

import com.example.demo.models.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private String id;
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime dateEnvoi;
    private String reservationId;
    private String clientId;
    private String type;
}
