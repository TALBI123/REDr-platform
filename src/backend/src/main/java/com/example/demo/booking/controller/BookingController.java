package com.example.demo.booking.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final SecurityUtils securityUtils;

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<String> myBookings() {
        UUID userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok("Bookings for user " + userId);
    }
}
