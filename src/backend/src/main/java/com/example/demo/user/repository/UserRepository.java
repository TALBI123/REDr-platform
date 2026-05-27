package com.example.demo.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.user.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
    
    Optional<AppUser> findByEmailAndDeletedAtIsNull(String email);
    
    Optional<AppUser> findByIdAndDeletedAtIsNull(UUID id);
    
    boolean existsByEmail(String email);
}