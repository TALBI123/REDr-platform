package com.example.demo.financial.service;

import com.example.demo.booking.repository.ReservationRepository;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.financial.dto.PaymentCreateRequestDTO;
import com.example.demo.financial.dto.PaymentResponseDTO;
import com.example.demo.financial.repository.PaymentRepository;
import com.example.demo.models.financial.Payment;
import com.example.demo.models.rental.Reservation;
import com.example.demo.models.user.Client;
import com.example.demo.user.repository.ClientRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public PaymentResponseDTO createPayment(String clientId, PaymentCreateRequestDTO request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + clientId));

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + request.getReservationId()));

        if (!reservation.getClient().getId().equals(clientId) && !securityUtils.isAdmin()) {
            throw new IllegalArgumentException("Cannot pay for someone else's reservation");
        }

        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setType(request.getType());
        payment.setDateEnvoi(LocalDateTime.now());
        payment.setReservation(reservation);
        payment.setClient(client);
        payment.setStatus(com.example.demo.models.enums.PaymentStatus.SUCCESS);

        Payment saved = paymentRepository.save(payment);

        // Optionally attach deposit amount to reservation
        if (reservation.getDepositAmount() == null || reservation.getDepositAmount().compareTo(saved.getAmount()) < 0) {
            reservation.setDepositAmount(saved.getAmount());
            reservationRepository.save(reservation);
        }

        return toDto(saved);
    }

    public Page<PaymentResponseDTO> getMyPayments(String clientId, Pageable pageable) {
        Page<Payment> page = paymentRepository.findByClientIdOrderByDateEnvoiDesc(clientId, pageable);
        return page.map(this::toDto);
    }

    public PaymentResponseDTO getPayment(String clientId, String paymentId, boolean admin) {
        Payment p = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        if (!admin && (p.getClient() == null || !p.getClient().getId().equals(clientId))) {
            throw new IllegalArgumentException("Access denied");
        }

        return toDto(p);
    }

    private PaymentResponseDTO toDto(Payment p) {
        return new PaymentResponseDTO(
                p.getId(),
                p.getStatus(),
                p.getAmount(),
                p.getDateEnvoi(),
                p.getReservation() != null ? p.getReservation().getId() : null,
                p.getClient() != null ? p.getClient().getId() : null,
                p.getType());
    }
}
