package com.example.demo.photos.controller;

import com.example.demo.photos.dto.PhotoResponseDTO;
import com.example.demo.photos.mapper.PhotoResponseMapper;
import com.example.demo.photos.service.PhotoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/vehicles/{carId}/photos")
@RequiredArgsConstructor
public class PublicPhotoController {

    private final PhotoService photoService;
    private final PhotoResponseMapper photoResponseMapper;

    @GetMapping
    public ResponseEntity<List<PhotoResponseDTO>> listCarPhotos(@PathVariable String carId) {
        return ResponseEntity.ok(photoResponseMapper.toDtos(photoService.listCarPhotos(carId)));
    }
}
