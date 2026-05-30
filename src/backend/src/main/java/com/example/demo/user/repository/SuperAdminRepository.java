package com.example.demo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.models.user.Admin;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<Admin, String> {
    
    // Récupérer un super admin par email
    Optional<Admin> findByEmail(String email);
    
    // Récupérer les admins avec certaines permissions
    List<Admin> findByPermissionsContaining(String permission);
    
    // Vérifier si un email est un super admin
    boolean existsByEmail(String email);
    
    // Récupérer tous les super admins triés par email
    List<Admin> findAllByOrderByEmailAsc();
    
    // Compter les super admins avec des permissions spécifiques
    long countByPermissionsLike(String permissionPattern);
}
