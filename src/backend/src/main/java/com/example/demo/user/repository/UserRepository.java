package com.example.demo.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.user.AppUser;
import com.example.demo.models.enums.UserRole;

public interface UserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);
    
    Optional<AppUser> findByEmailAndDeletedAtIsNull(String email);
    
    Optional<AppUser> findByIdAndDeletedAtIsNull(String id);
    
    boolean existsByEmail(String email);

    List<AppUser> findByRole(UserRole role);
}