package com.example.demo.cars.controller;

import com.example.demo.cars.dto.CarRequestDTO;
import com.example.demo.cars.dto.CarResponseDTO;
import com.example.demo.cars.dto.CarSearchCriteriaDTO;
import com.example.demo.cars.dto.CarStatusUpdateDTO;
import com.example.demo.cars.dto.CarUpdateDTO;
import com.example.demo.cars.mapper.CarResponseMapper;
import com.example.demo.cars.service.CarService;
import com.example.demo.models.agency.Car;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/vehicles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminCarController {

	private final CarService carService;
	private final CarResponseMapper carResponseMapper;

	@GetMapping
	public ResponseEntity<Page<CarResponseDTO>> listCars(
			CarSearchCriteriaDTO criteria,
			Pageable pageable) {
		Page<CarResponseDTO> result = carService
				.searchCars(criteria, pageable)
				.map(carResponseMapper::toDto);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{carId}")
	public ResponseEntity<CarResponseDTO> getCar(@PathVariable String carId) {
		Car car = carService.getById(carId);
		return ResponseEntity.ok(carResponseMapper.toDto(car));
	}

	@PostMapping
	public ResponseEntity<CarResponseDTO> createCar(
			@Valid @RequestBody CarRequestDTO request) {
		Car car = carService.createCar(request);
		return ResponseEntity.status(201).body(carResponseMapper.toDto(car));
	}

	@PutMapping("/{carId}")
	public ResponseEntity<CarResponseDTO> updateCar(
			@PathVariable String carId,
			@Valid @RequestBody CarUpdateDTO request) {
		Car car = carService.updateCar(carId, request);
		return ResponseEntity.ok(carResponseMapper.toDto(car));
	}

	@PatchMapping("/{carId}/status")
	public ResponseEntity<CarResponseDTO> updateCarStatus(
			@PathVariable String carId,
			@RequestBody CarStatusUpdateDTO request) {
		Car car = carService.updateStatus(carId, request);
		return ResponseEntity.ok(carResponseMapper.toDto(car));
	}

	@DeleteMapping("/{carId}")
	public ResponseEntity<Void> deleteCar(@PathVariable String carId) {
		carService.deleteCar(carId);
		return ResponseEntity.noContent().build();
	}
}
