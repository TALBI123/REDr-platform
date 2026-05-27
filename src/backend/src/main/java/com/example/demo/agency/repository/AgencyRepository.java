package com.example.demo.agency.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.agency.entity.Agency;
import com.example.demo.agency.entity.AgencyStatus;

public interface AgencyRepository extends JpaRepository<Agency, UUID> {
    Optional<Agency> findByIdAndStatus(UUID id, AgencyStatus status);
    Optional<Agency> findByName(String name);
}