package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
