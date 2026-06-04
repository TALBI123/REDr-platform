package com.example.demo.booking.controller;

import com.example.demo.booking.dto.BookingCancelRequestDTO;
import com.example.demo.booking.dto.BookingResponseDTO;
import com.example.demo.booking.service.BookingService;
import com.example.demo.models.rental.Reservation;
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
@RequestMapping("/agencies/{agencyId}/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'AGENCY_MANAGER')")
public class AgencyBookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<Page<BookingResponseDTO>> listAgencyBookings(
            @PathVariable String agencyId,
            Pageable pageable) {
        Page<Reservation> reservations = bookingService.getAgencyBookings(agencyId, pageable);
        return ResponseEntity.ok(reservations.map(this::toDto));
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(
            @PathVariable String agencyId,
            @PathVariable String bookingId) {
        Reservation reservation = bookingService.confirmBooking(agencyId, bookingId);
        return ResponseEntity.ok(toDto(reservation));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable String agencyId,
            @PathVariable String bookingId,
            @RequestBody(required = false) BookingCancelRequestDTO request) {
        Reservation reservation = bookingService.cancelAgencyBooking(agencyId, bookingId, request);
        return ResponseEntity.ok(toDto(reservation));
    }

    private BookingResponseDTO toDto(Reservation reservation) {
        return new BookingResponseDTO(
                reservation.getId(),
                reservation.getStatus(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getCreationDate(),
                reservation.getCompletedAt(),
                reservation.getTotalAmount(),
                reservation.getPickupLocation(),
                reservation.getReturnLocation(),
                reservation.getCancellationReason(),
                reservation.getClient() != null ? reservation.getClient().getId() : null,
                reservation.getCar() != null ? reservation.getCar().getId() : null,
                reservation.getCar() != null ? reservation.getCar().getBrand() : null,
                reservation.getCar() != null ? reservation.getCar().getMode() : null,
                reservation.getCar() != null && reservation.getCar().getAgency() != null
                        ? reservation.getCar().getAgency().getId()
                        : null,
                reservation.getCar() != null && reservation.getCar().getAgency() != null
                        ? reservation.getCar().getAgency().getName()
                        : null);
    }
}