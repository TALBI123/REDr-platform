package com.example.demo.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<String> listUsers() {
        return ResponseEntity.ok("Admin: list all users");
    }

    @GetMapping("/agencies")
    public ResponseEntity<String> listAgencies() {
        return ResponseEntity.ok("Admin: list all agencies");
    }
}
