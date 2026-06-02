package com.example.demo.photos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.demo.cars.repository.CarRepository;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.models.agency.Agency;
import com.example.demo.models.agency.Car;
import com.example.demo.models.agency.Photo;
import com.example.demo.photos.repository.PhotoRepository;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

/*
 * Unit tests for PhotoService.
 * These tests cover Cloudinary upload mapping, ownership checks, and error cases.
 * Cloudinary and repository calls are mocked to keep tests deterministic and fast.
 */
@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private PhotoService photoService;

    @Test
    void listCarPhotos_requiresExistingCar() {
        when(carRepository.existsById("car-1")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> photoService.listCarPhotos("car-1"));
    }

    @Test
    void uploadCarPhoto_requiresFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpeg",
                new byte[0]);

        assertThrows(IllegalArgumentException.class,
                () -> photoService.uploadCarPhoto("agency-1", "car-1", file, null));

        verifyNoInteractions(carRepository);
    }

    @Test
    void uploadCarPhoto_deniesWrongAgency() {
        Agency agency = new Agency();
        agency.setId("agency-1");

        Car car = new Car();
        car.setAgency(agency);

        when(carRepository.findById("car-1")).thenReturn(Optional.of(car));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.jpg",
                "image/jpeg",
                "content".getBytes());

        assertThrows(AccessDeniedException.class,
                () -> photoService.uploadCarPhoto("agency-2", "car-1", file, "front view"));

        verifyNoInteractions(photoRepository);
        verifyNoInteractions(cloudinary);
    }

    @Test
    void uploadCarPhoto_uploadsToCloudinaryAndSaves() throws Exception {
        Agency agency = new Agency();
        agency.setId("agency-1");

        Car car = new Car();
        car.setAgency(agency);

        when(carRepository.findById("car-1")).thenReturn(Optional.of(car));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.jpg",
                "image/jpeg",
                "content".getBytes());

        Uploader uploader = org.mockito.Mockito.mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(Map.of(
                "url", "http://example.com/raw.jpg",
                "secure_url", "https://example.com/secure.jpg",
                "public_id", "cloud-123"));

        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReflectionTestUtils.setField(photoService, "cloudinaryFolder", "cars");

        Photo saved = photoService.uploadCarPhoto("agency-1", "car-1", file, "front view");

        assertEquals("cloud-123", saved.getPublicId());
        assertEquals("front view", saved.getDescription());
        assertSame(car, saved.getCar());
    }

    @Test
    void deleteCarPhoto_requiresMatchingCar() {
        Photo photo = new Photo();
        Car car = new Car();
        car.setId("car-1");
        photo.setCar(car);

        when(photoRepository.findById("photo-1")).thenReturn(Optional.of(photo));

        assertThrows(ResourceNotFoundException.class,
                () -> photoService.deleteCarPhoto("agency-1", "car-2", "photo-1"));

        verify(photoRepository, never()).delete(any(Photo.class));
    }
}
