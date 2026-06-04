package com.example.demo.financial.repository;

import com.example.demo.models.financial.Payment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Page<Payment> findByClientIdOrderByDateEnvoiDesc(String clientId, Pageable pageable);

    List<Payment> findByReservationId(String reservationId);
}
