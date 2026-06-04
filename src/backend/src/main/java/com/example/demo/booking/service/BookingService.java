package com.example.demo.booking.service;

import com.example.demo.booking.dto.BookingCancelRequestDTO;
import com.example.demo.booking.dto.BookingCreateRequestDTO;
import com.example.demo.booking.dto.BookingUpdateRequestDTO;
import com.example.demo.booking.repository.ReservationRepository;
import com.example.demo.cars.repository.CarRepository;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.models.agency.Car;
import com.example.demo.models.enums.ReservationStatus;
import com.example.demo.models.enums.VehicleStatus;
import com.example.demo.models.rental.Reservation;
import com.example.demo.models.user.Client;
import com.example.demo.user.repository.ClientRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private static final List<ReservationStatus> ACTIVE_RESERVATION_STATUSES = List.of(
            ReservationStatus.PENDING,
            ReservationStatus.CONFIRMED);

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public Reservation createBooking(String clientId, BookingCreateRequestDTO request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + clientId));
        Car car = getBookableCar(request.getCarId());
        validateBookingDates(request.getStartDate(), request.getEndDate());
        ensureNoOverlap(car.getId(), request.getStartDate(), request.getEndDate(), null);

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setCar(car);
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setPickupLocation(request.getPickupLocation());
        reservation.setReturnLocation(request.getReturnLocation());
        reservation.setCreationDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setTotalAmount(calculateTotalAmount(car, request.getStartDate(), request.getEndDate()));

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation updateBooking(String clientId, String bookingId, BookingUpdateRequestDTO request) {
        Reservation reservation = reservationRepository.findByIdAndClientId(bookingId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Only pending bookings can be modified");
        }

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : reservation.getStartDate();
        LocalDate endDate = request.getEndDate() != null ? request.getEndDate() : reservation.getEndDate();
        validateBookingDates(startDate, endDate);
        ensureNoOverlap(reservation.getCar().getId(), startDate, endDate, reservation.getId());

        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        if (request.getPickupLocation() != null) {
            reservation.setPickupLocation(request.getPickupLocation());
        }
        if (request.getReturnLocation() != null) {
            reservation.setReturnLocation(request.getReturnLocation());
        }
        reservation.setTotalAmount(calculateTotalAmount(reservation.getCar(), startDate, endDate));

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelBooking(String clientId, String bookingId, BookingCancelRequestDTO request) {
        Reservation reservation = reservationRepository.findByIdAndClientId(bookingId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        cancelReservation(reservation, request != null ? request.getReason() : null);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation confirmBooking(String agencyId, String bookingId) {
        securityUtils.assertAgencyAccess(agencyId);
        Reservation reservation = reservationRepository.findByIdAndCarAgencyId(bookingId, agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Only pending bookings can be confirmed");
        }

        ensureNoOverlap(reservation.getCar().getId(), reservation.getStartDate(), reservation.getEndDate(), reservation.getId());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelAgencyBooking(String agencyId, String bookingId, BookingCancelRequestDTO request) {
        securityUtils.assertAgencyAccess(agencyId);
        Reservation reservation = reservationRepository.findByIdAndCarAgencyId(bookingId, agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        cancelReservation(reservation, request != null ? request.getReason() : null);
        return reservationRepository.save(reservation);
    }

    public Page<Reservation> getMyBookings(String userId, boolean admin, Pageable pageable) {
        if (admin) {
            return reservationRepository.findAll(pageable);
        }
        return reservationRepository.findByClientIdOrderByStartDateDesc(userId, pageable);
    }

    public Page<Reservation> getAgencyBookings(String agencyId, Pageable pageable) {
        securityUtils.assertAgencyAccess(agencyId);
        return reservationRepository.findByCarAgencyIdOrderByStartDateDesc(agencyId, pageable);
    }

    public Reservation getMyBooking(String userId, boolean admin, String bookingId) {
        if (admin) {
            return reservationRepository.findById(bookingId)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        }

        return reservationRepository.findByIdAndClientId(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
    }

    public Reservation getAgencyBooking(String agencyId, String bookingId) {
        securityUtils.assertAgencyAccess(agencyId);
        return reservationRepository.findByIdAndCarAgencyId(bookingId, agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
    }

    private Car getBookableCar(String carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found: " + carId));

        if (car.getCurrentStatus() != VehicleStatus.Available
                && car.getCurrentStatus() != VehicleStatus.AvailableSoon) {
            throw new IllegalArgumentException("Car is not available for booking");
        }

        return car;
    }

    private void validateBookingDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates are required");
        }
        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    private void ensureNoOverlap(String carId, LocalDate startDate, LocalDate endDate, String excludeReservationId) {
        boolean overlap = reservationRepository.existsOverlappingReservation(
                carId,
                startDate,
                endDate,
                ACTIVE_RESERVATION_STATUSES,
                excludeReservationId);

        if (overlap) {
            throw new IllegalArgumentException("Car already has a reservation for the selected dates");
        }
    }

    private void cancelReservation(Reservation reservation, String reason) {
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return;
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed bookings cannot be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        if (reason != null && !reason.isBlank()) {
            reservation.setCancellationReason(reason.trim());
        }
    }

    private BigDecimal calculateTotalAmount(Car car, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            throw new IllegalArgumentException("Booking duration must be at least one day");
        }

        BigDecimal total = BigDecimal.ZERO;
        long remainingDays = days;

        if (remainingDays >= 30 && car.getMonthlyPrice() != null) {
            long months = remainingDays / 30;
            total = total.add(car.getMonthlyPrice().multiply(BigDecimal.valueOf(months)));
            remainingDays = remainingDays % 30;
        }

        if (remainingDays >= 7 && car.getWeeklyPrice() != null) {
            long weeks = remainingDays / 7;
            total = total.add(car.getWeeklyPrice().multiply(BigDecimal.valueOf(weeks)));
            remainingDays = remainingDays % 7;
        }

        if (remainingDays > 0) {
            if (car.getDailyPrice() == null) {
                throw new IllegalArgumentException("Car daily price is required to compute booking total");
            }
            total = total.add(car.getDailyPrice().multiply(BigDecimal.valueOf(remainingDays)));
        }

        if (isPromotionApplicable(car, startDate, endDate)) {
            BigDecimal promotionRate = car.getPromotionRate() == null ? BigDecimal.ZERO : car.getPromotionRate();
            BigDecimal discount = total.multiply(promotionRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            total = total.subtract(discount);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isPromotionApplicable(Car car, LocalDate startDate, LocalDate endDate) {
        if (car.getPromotionActive() == null || !car.getPromotionActive()) {
            return false;
        }
        if (car.getPromotionStartDate() == null || car.getPromotionEndDate() == null) {
            return false;
        }
        return !car.getPromotionStartDate().isAfter(endDate) && !car.getPromotionEndDate().isBefore(startDate);
    }
}