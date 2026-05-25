package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.AppRole;

public interface AppRoleRepo extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String roleName);
    
}
