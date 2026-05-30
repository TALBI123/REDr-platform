package com.example.demo.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.user.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    
    // Récupérer un client par email
    Optional<Client> findByEmail(String email);
    
    // Récupérer tous les clients d'un manager spécifique
    // NOTE: Client entity does not have a 'manager' property. If you need
    // to query by manager, add an appropriate relation to the Client entity
    // (e.g. private AgencyManager manager;) and then use a derived query
    // like List<Client> findByManager_Id(String managerId) or implement a
    // custom @Query.

    
  
    // Vérifier si un client existe par email
    boolean existsByEmail(String email);
}
