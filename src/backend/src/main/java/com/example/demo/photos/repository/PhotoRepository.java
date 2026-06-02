package com.example.demo.photos.repository;

import com.example.demo.models.agency.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, String> {
    List<Photo> findByCarId(String carId);
}
