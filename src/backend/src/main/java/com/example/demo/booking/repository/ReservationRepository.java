package com.example.demo.booking.repository;

import com.example.demo.models.enums.ReservationStatus;
import com.example.demo.models.rental.Reservation;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    Page<Reservation> findByClientIdOrderByStartDateDesc(String clientId, Pageable pageable);

    Page<Reservation> findByCarAgencyIdOrderByStartDateDesc(String agencyId, Pageable pageable);

    Optional<Reservation> findByIdAndClientId(String id, String clientId);

    Optional<Reservation> findByIdAndCarAgencyId(String id, String agencyId);

    @Query("""
            select count(r) > 0
            from Reservation r
            where r.car.id = :carId
              and r.status in :activeStatuses
              and r.startDate <= :endDate
              and r.endDate >= :startDate
              and (:excludeReservationId is null or r.id <> :excludeReservationId)
            """)
    // /cars/{carId}/availability?startDate=2024-07-01&endDate=2024-07-10
    boolean existsOverlappingReservation(
            @Param("carId") String carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("activeStatuses") Collection<ReservationStatus> activeStatuses,
            @Param("excludeReservationId") String excludeReservationId);

    @Query("""
            select r
            from Reservation r
            where r.car.agency.id = :agencyId
              and r.status in :statuses
            order by r.startDate desc
            """)
    Page<Reservation> findByAgencyAndStatuses(
            @Param("agencyId") String agencyId,
            @Param("statuses") List<ReservationStatus> statuses,
            Pageable pageable);
}