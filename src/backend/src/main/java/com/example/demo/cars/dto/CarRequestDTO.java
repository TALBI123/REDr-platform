package com.example.demo.cars.dto;

import com.example.demo.models.enums.ConditionStatus;
import com.example.demo.models.enums.FuelType;
import com.example.demo.models.enums.TransmissionType;
import com.example.demo.models.enums.VehicleStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDTO {

	@NotBlank
	@Size(max = 120)
	private String brand;

	@NotBlank
	@Size(max = 120)
	private String mode;

	@NotNull
	private Integer year;

	private List<FuelType> fuelTypes;

	@NotNull
	private TransmissionType transmissionType;

	private Integer mileage;

	@DecimalMin("0.0")
	private BigDecimal dailyPrice;

	@DecimalMin("0.0")
	private BigDecimal weeklyPrice;

	@DecimalMin("0.0")
	private BigDecimal monthlyPrice;

	private Boolean promotionActive;

	@DecimalMin("0.0")
	private BigDecimal promotionRate;

	private LocalDate promotionStartDate;
	private LocalDate promotionEndDate;

	private ConditionStatus conditionStatus;

	private LocalDate licenseExpiryDate;
	private LocalDate insuranceExpiryDate;

	private String registrationNumber;
	private String insurancePolicyNumber;

	private VehicleStatus currentStatus;

	private Integer seatCapacity;

	@Size(max = 4000)
	private String technicalNotes;

	@Size(max = 4000)
	private String description;

	private String agencyId;
	private String categoryId;
}
