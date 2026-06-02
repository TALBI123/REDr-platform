package com.example.demo.cars.controller;

import com.example.demo.cars.dto.CarResponseDTO;
import com.example.demo.cars.dto.CarSearchCriteriaDTO;
import com.example.demo.cars.mapper.CarResponseMapper;
import com.example.demo.cars.service.CarService;
import com.example.demo.models.agency.Car;
import com.example.demo.models.enums.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/vehicles")
@RequiredArgsConstructor
public class PublicCarController {

	private final CarService carService;
	private final CarResponseMapper carResponseMapper;

	@GetMapping
	public ResponseEntity<Page<CarResponseDTO>> listPublicCars(
			CarSearchCriteriaDTO criteria,
			Pageable pageable) {
		if (criteria.getStatus() == null) {
			criteria.setStatus(VehicleStatus.Available);
		}
		Page<CarResponseDTO> result = carService
				.searchCars(criteria, pageable)
				.map(carResponseMapper::toDto);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{carId}")
	public ResponseEntity<CarResponseDTO> getPublicCar(@PathVariable String carId) {
		Car car = carService.getById(carId);
		return ResponseEntity.ok(carResponseMapper.toDto(car));
	}
}
