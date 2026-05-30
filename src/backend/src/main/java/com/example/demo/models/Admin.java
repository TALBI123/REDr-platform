package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("SUPER_ADMIN")
@Table(name = "admins")
public class Admin extends AppUser {

    @Column(name = "permissions")
    private String permissions;

    @Column(name = "admin_level")
    private int adminLevel;
}