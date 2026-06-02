package com.example.demo.photos.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.cars.repository.CarRepository;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.models.agency.Car;
import com.example.demo.models.agency.Photo;
import com.example.demo.photos.dto.PhotoUpdateDTO;
import com.example.demo.photos.repository.PhotoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final CarRepository carRepository;
    private final Cloudinary cloudinary;

    @Value("${app.cloudinary.folder:cars}")
    private String cloudinaryFolder;

    public List<Photo> listCarPhotos(String carId) {
        if (!carRepository.existsById(carId)) {
            throw new ResourceNotFoundException("Car not found: " + carId);
        }
        return photoRepository.findByCarId(carId);
    }

    public Photo getById(String photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found: " + photoId));
    }

    @Transactional
    public Photo uploadCarPhoto(String agencyId, String carId, MultipartFile file, String description) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Photo file is required");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found: " + carId));
        assertCarAgency(car, agencyId);

        Map<String, Object> uploadResult = uploadToCloudinary(file);
        Photo photo = new Photo();
        photo.setUrl((String) uploadResult.get("url"));
        photo.setSecureUrl((String) uploadResult.get("secure_url"));
        photo.setPublicId((String) uploadResult.get("public_id"));
        photo.setDescription(description);
        photo.setCar(car);
        return photoRepository.save(photo);
    }

    @Transactional
    public Photo updateCarPhoto(String agencyId, String carId, String photoId, PhotoUpdateDTO request) {
        Photo photo = getById(photoId);
        if (photo.getCar() == null || !carId.equals(photo.getCar().getId())) {
            throw new ResourceNotFoundException("Photo not found: " + photoId);
        }
        assertCarAgency(photo.getCar(), agencyId);
        photo.setDescription(request.getDescription());
        return photoRepository.save(photo);
    }

    @Transactional
    public void deleteCarPhoto(String agencyId, String carId, String photoId) {
        Photo photo = getById(photoId);
        if (photo.getCar() == null || !carId.equals(photo.getCar().getId())) {
            throw new ResourceNotFoundException("Photo not found: " + photoId);
        }
        assertCarAgency(photo.getCar(), agencyId);
        deleteFromCloudinary(photo.getPublicId());
        photoRepository.delete(photo);
    }

    private void assertCarAgency(Car car, String agencyId) {
        if (car.getAgency() == null || !car.getAgency().getId().equals(agencyId)) {
            throw new AccessDeniedException("Access denied: car does not belong to agency");
        }
    }

    private Map<String, Object> uploadToCloudinary(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", cloudinaryFolder,
                            "resource_type", "image"));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to upload photo", ex);
        }
    }

    private void deleteFromCloudinary(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to delete photo", ex);
        }
    }
}
