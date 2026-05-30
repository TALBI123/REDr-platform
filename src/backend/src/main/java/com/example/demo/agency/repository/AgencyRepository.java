package com.example.demo.agency.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Agency;
import com.example.demo.models.enums.AgencyStatus;

public interface AgencyRepository extends JpaRepository<Agency, String> {
    Optional<Agency> findByIdAndStatus(String id, AgencyStatus status);
    Optional<Agency> findByName(String name);
    List<Agency> findByStatus(AgencyStatus status);
}