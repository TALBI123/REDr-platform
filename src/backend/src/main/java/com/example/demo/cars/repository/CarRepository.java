package com.example.demo.cars.repository;

import com.example.demo.models.agency.Car;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CarRepository extends JpaRepository<Car, String>, JpaSpecificationExecutor<Car> {
    Optional<Car> findByIdAndAgencyId(String id, String agencyId);
}
