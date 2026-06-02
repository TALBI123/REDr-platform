package com.example.demo.photos.controller;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.photos.dto.PhotoResponseDTO;
import com.example.demo.photos.dto.PhotoUpdateDTO;
import com.example.demo.photos.mapper.PhotoResponseMapper;
import com.example.demo.photos.service.PhotoService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/agencies/{agencyId}/vehicles/{carId}/photos")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN','AGENCY_MANAGER')")
public class AgencyPhotoController {

    private final PhotoService photoService;
    private final PhotoResponseMapper photoResponseMapper;
    private final SecurityUtils securityUtils;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoResponseDTO> uploadCarPhoto(
            @PathVariable String agencyId,
            @PathVariable String carId,
            @NotNull @RequestPart("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {
        securityUtils.assertAgencyAccess(agencyId);
        return ResponseEntity.status(201)
                .body(photoResponseMapper.toDto(photoService.uploadCarPhoto(agencyId, carId, file, description)));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deleteCarPhoto(
            @PathVariable String agencyId,
            @PathVariable String carId,
            @PathVariable String photoId) {
        securityUtils.assertAgencyAccess(agencyId);
        photoService.deleteCarPhoto(agencyId, carId, photoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{photoId}")
    public ResponseEntity<PhotoResponseDTO> updateCarPhoto(
            @PathVariable String agencyId,
            @PathVariable String carId,
            @PathVariable String photoId,
            @RequestBody PhotoUpdateDTO request) {
        securityUtils.assertAgencyAccess(agencyId);
        return ResponseEntity.ok(
                photoResponseMapper.toDto(photoService.updateCarPhoto(agencyId, carId, photoId, request)));
    }
}
