package com.example.demo.cars.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.agency.repository.AgencyRepository;
import com.example.demo.agency.repository.CategoryRepository;
import com.example.demo.cars.dto.CarRequestDTO;
import com.example.demo.cars.dto.CarUpdateDTO;
import com.example.demo.cars.mapper.CarCommandMapper;
import com.example.demo.cars.repository.CarRepository;
import com.example.demo.models.agency.Agency;
import com.example.demo.models.agency.Car;
import com.example.demo.models.agency.Category;
import com.example.demo.models.enums.VehicleStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * Unit tests for CarService.
 * These tests validate default value handling, relation attachment, and status changes.
 * The repository and mapper dependencies are mocked to keep tests focused on service logic.
 */
@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private AgencyRepository agencyRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CarCommandMapper carCommandMapper;

    @InjectMocks
    private CarService carService;

    @Test
    void createCar_requiresAgencyId() {
        CarRequestDTO request = new CarRequestDTO();
        request.setAgencyId(" ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> carService.createCar(request));

        assertEquals("Agency id is required", ex.getMessage());
        verifyNoInteractions(carRepository);
    }

    @Test
    void createCar_setsDefaultsAndLinksAgencyAndCategory() {
        CarRequestDTO request = new CarRequestDTO();
        request.setAgencyId("agency-1");
        request.setCategoryId("category-1");

        Car car = new Car();
        when(carCommandMapper.toEntity(request)).thenReturn(car);

        Agency agency = new Agency();
        agency.setId("agency-1");
        Category category = new Category();
        category.setId("category-1");

        when(agencyRepository.findById("agency-1")).thenReturn(Optional.of(agency));
        when(categoryRepository.findById("category-1")).thenReturn(Optional.of(category));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Car saved = carService.createCar(request);

        assertEquals(VehicleStatus.Available, saved.getCurrentStatus());
        assertNotNull(saved.getFuelTypes());
        assertSame(agency, saved.getAgency());
        assertSame(category, saved.getCategory());
    }

    @Test
    void updateCarForAgency_usesScopedRepositoryAndMapper() {
        CarUpdateDTO request = new CarUpdateDTO();

        Agency agency = new Agency();
        agency.setId("agency-1");

        Car car = new Car();
        car.setAgency(agency);

        when(carRepository.findByIdAndAgencyId("car-1", "agency-1")).thenReturn(Optional.of(car));
        when(agencyRepository.findById("agency-1")).thenReturn(Optional.of(agency));
        when(carRepository.save(car)).thenReturn(car);

        Car updated = carService.updateCarForAgency("car-1", "agency-1", request);

        verify(carCommandMapper).updateEntity(request, car);
        verify(carRepository).save(car);
        assertSame(car, updated);
    }
}
