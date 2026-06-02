package com.example.demo.cars.dto;

import com.example.demo.models.enums.ConditionStatus;
import com.example.demo.models.enums.FuelType;
import com.example.demo.models.enums.TransmissionType;
import com.example.demo.models.enums.VehicleStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDTO {

	private String id;
	private String brand;
	private String mode;
	private Integer year;
	private List<FuelType> fuelTypes;
	private TransmissionType transmissionType;
	private Integer mileage;
	private VehicleStatus currentStatus;
	private BigDecimal dailyPrice;
	private BigDecimal weeklyPrice;
	private BigDecimal monthlyPrice;
	private Boolean promotionActive;
	private BigDecimal promotionRate;
	private LocalDate promotionStartDate;
	private LocalDate promotionEndDate;
	private ConditionStatus conditionStatus;
	private LocalDate licenseExpiryDate;
	private LocalDate insuranceExpiryDate;
	private String registrationNumber;
	private String insurancePolicyNumber;
	private BigDecimal averageRating;
	private Integer seatCapacity;
	private String technicalNotes;
	private String description;
	private String agencyId;
	private String categoryId;
	private List<String> photoUrls;
}
