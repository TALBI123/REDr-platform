package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseUuidEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @PrePersist
    protected void assignIdIfNecessary() {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
    }
}