package com.example.demo.cars.controller;

import com.example.demo.cars.dto.CarRequestDTO;
import com.example.demo.cars.dto.CarResponseDTO;
import com.example.demo.cars.dto.CarSearchCriteriaDTO;
import com.example.demo.cars.dto.CarStatusUpdateDTO;
import com.example.demo.cars.dto.CarUpdateDTO;
import com.example.demo.cars.mapper.CarResponseMapper;
import com.example.demo.cars.service.CarService;
import com.example.demo.common.security.SecurityUtils;
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
@RequestMapping("/agencies/{agencyId}/vehicles")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN','AGENCY_MANAGER')")
public class AgencyCarController {

	private final CarService carService;
	private final CarResponseMapper carResponseMapper;
	private final SecurityUtils securityUtils;

	@GetMapping
	public ResponseEntity<Page<CarResponseDTO>> listAgencyCars(
			@PathVariable String agencyId,
			CarSearchCriteriaDTO criteria,
			Pageable pageable) {
		securityUtils.assertAgencyAccess(agencyId);
		criteria.setAgencyId(agencyId);
		Page<CarResponseDTO> result = carService
				.searchCars(criteria, pageable)
				.map(carResponseMapper::toDto);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{carId}")
	public ResponseEntity<CarResponseDTO> getAgencyCar(
			@PathVariable String agencyId,
			@PathVariable String carId) {
		securityUtils.assertAgencyAccess(agencyId);
		Car car = carService.getByIdAndAgency(carId, agencyId);
		return ResponseEntity.ok(carResponseMapper.toDto(car));
	}

	@PostMapping
	public ResponseEntity<CarResponseDTO> createAgencyCar(
			@PathVariable String agencyId,
			@Valid @RequestBody CarRequestDTO request) {
		securityUtils.assertAgencyAccess(agencyId);
		Car car = carService.createCarForAgency(agencyId, request);
		return ResponseEntity.status(201).body(carResponseMapper.toDto(car));
	}

	@PutMapping("/{carId}")
	public ResponseEntity<CarResponseDTO> updateAgencyCar(
			@PathVariable String agencyId,
			@PathVariable String carId,
			@Valid @RequestBody CarUpdateDTO request) {
		securityUtils.assertAgencyAccess(agencyId);
		Car car = carService.updateCarForAgency(carId, agencyId, request);
		return ResponseEntity.ok(carResponseMapper.toDto(car));
	}

	@PatchMapping("/{carId}/status")
	public ResponseEntity<CarResponseDTO> updateAgencyCarStatus(
			@PathVariable String agencyId,
			@PathVariable String carId,
			@RequestBody CarStatusUpdateDTO request) {
		securityUtils.assertAgencyAccess(agencyId);
		Car car = carService.updateStatusForAgency(carId, agencyId, request);
		return ResponseEntity.ok(carResponseMapper.toDto(car));
	}

	@DeleteMapping("/{carId}")
	public ResponseEntity<Void> deleteAgencyCar(
			@PathVariable String agencyId,
			@PathVariable String carId) {
		securityUtils.assertAgencyAccess(agencyId);
		carService.deleteCarForAgency(carId, agencyId);
		return ResponseEntity.noContent().build();
	}
}
