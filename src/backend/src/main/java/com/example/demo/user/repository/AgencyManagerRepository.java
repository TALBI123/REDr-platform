package com.example.demo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.user.AgencyManager;

// import java.util.List;
import java.util.Optional;

@Repository
public interface AgencyManagerRepository extends JpaRepository<AgencyManager, String> {
    
    // Récupérer un manager par email
    Optional<AgencyManager> findByEmail(String email);
    
    // // Récupérer les managers par département
    // List<AgencyManager> findByDepartment(String department);
    

    // // Rechercher par nom (si attribut name existe)
    // List<AgencyManager> findByNameContainingIgnoreCase(String name);
    
    // // Compter les managers par département
    // long countByDepartment(String department);

}
