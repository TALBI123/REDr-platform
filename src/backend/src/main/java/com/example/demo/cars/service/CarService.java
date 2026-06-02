package com.example.demo.cars.service;

import com.example.demo.agency.repository.AgencyRepository;
import com.example.demo.agency.repository.CategoryRepository;
import com.example.demo.cars.dto.CarRequestDTO;
import com.example.demo.cars.dto.CarSearchCriteriaDTO;
import com.example.demo.cars.dto.CarStatusUpdateDTO;
import com.example.demo.cars.dto.CarUpdateDTO;
import com.example.demo.cars.mapper.CarCommandMapper;
import com.example.demo.cars.repository.CarRepository;
import com.example.demo.cars.repository.CarSpecifications;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.models.agency.Agency;
import com.example.demo.models.agency.Car;
import com.example.demo.models.agency.Category;
import com.example.demo.models.enums.VehicleStatus;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final AgencyRepository agencyRepository;
    private final CategoryRepository categoryRepository;
    private final CarCommandMapper carCommandMapper;

    public Page<Car> searchCars(CarSearchCriteriaDTO criteria, Pageable pageable) {
        Specification<Car> spec = CarSpecifications.from(criteria);
        return carRepository.findAll(spec, pageable);
    }

    public Car getById(String carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found: " + carId));
    }

    public Car getByIdAndAgency(String carId, String agencyId) {
        return carRepository.findByIdAndAgencyId(carId, agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found: " + carId));
    }

    @Transactional
    public Car createCar(CarRequestDTO request) {
        if (request.getAgencyId() == null || request.getAgencyId().isBlank()) {
            throw new IllegalArgumentException("Agency id is required");
        }
        Car car = carCommandMapper.toEntity(request);
        applyDefaults(car);
        attachAgencyAndCategory(car, request.getAgencyId(), request.getCategoryId());
        return carRepository.save(car);
    }

    @Transactional
    public Car createCarForAgency(String agencyId, CarRequestDTO request) {
        Car car = carCommandMapper.toEntity(request);
        applyDefaults(car);
        attachAgencyAndCategory(car, agencyId, request.getCategoryId());
        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(String carId, CarUpdateDTO request) {
        Car car = getById(carId);
        carCommandMapper.updateEntity(request, car);
        attachAgencyAndCategory(car, car.getAgency() != null ? car.getAgency().getId() : null, request.getCategoryId());
        return carRepository.save(car);
    }

    @Transactional
    public Car updateCarForAgency(String carId, String agencyId, CarUpdateDTO request) {
        Car car = getByIdAndAgency(carId, agencyId);
        carCommandMapper.updateEntity(request, car);
        attachAgencyAndCategory(car, agencyId, request.getCategoryId());
        return carRepository.save(car);
    }

    @Transactional
    public Car updateStatus(String carId, CarStatusUpdateDTO request) {
        Car car = getById(carId);
        applyStatusUpdate(car, request);
        return carRepository.save(car);
    }

    @Transactional
    public Car updateStatusForAgency(String carId, String agencyId, CarStatusUpdateDTO request) {
        Car car = getByIdAndAgency(carId, agencyId);
        applyStatusUpdate(car, request);
        return carRepository.save(car);
    }

    @Transactional
    public void deleteCar(String carId) {
        Car car = getById(carId);
        carRepository.delete(car);
    }

    @Transactional
    public void deleteCarForAgency(String carId, String agencyId) {
        Car car = getByIdAndAgency(carId, agencyId);
        carRepository.delete(car);
    }

    private void applyDefaults(Car car) {
        if (car.getCurrentStatus() == null) {
            car.setCurrentStatus(VehicleStatus.Available);
        }
        if (car.getFuelTypes() == null) {
            car.setFuelTypes(new ArrayList<>());
        }
    }

    private void applyStatusUpdate(Car car, CarStatusUpdateDTO request) {
        if (request.getCurrentStatus() != null) {
            car.setCurrentStatus(request.getCurrentStatus());
        }
        if (request.getConditionStatus() != null) {
            car.setConditionStatus(request.getConditionStatus());
        }
    }

    private void attachAgencyAndCategory(Car car, String agencyId, String categoryId) {
        if (agencyId != null && !agencyId.isBlank()) {
            Agency agency = agencyRepository.findById(agencyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Agency not found: " + agencyId));
            car.setAgency(agency);
        }

        if (categoryId != null && !categoryId.isBlank()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
            car.setCategory(category);
        }
    }
}
