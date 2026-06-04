package com.example.demo.financial.controller;

import com.example.demo.financial.dto.PaymentCreateRequestDTO;
import com.example.demo.financial.dto.PaymentResponseDTO;
import com.example.demo.financial.service.PaymentService;
import com.example.demo.common.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityUtils securityUtils;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentCreateRequestDTO request) {
        PaymentResponseDTO dto = paymentService.createPayment(securityUtils.getCurrentUserId(), request);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<PaymentResponseDTO>> myPayments(Pageable pageable) {
        Page<PaymentResponseDTO> page = paymentService.getMyPayments(securityUtils.getCurrentUserId(), pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('CLIENT','SUPER_ADMIN')")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable String paymentId) {
        PaymentResponseDTO dto = paymentService.getPayment(securityUtils.getCurrentUserId(), paymentId, securityUtils.isAdmin());
        return ResponseEntity.ok(dto);
    }
}
