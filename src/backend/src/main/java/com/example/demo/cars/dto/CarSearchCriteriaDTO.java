package com.example.demo.cars.dto;

import com.example.demo.models.enums.ConditionStatus;
import com.example.demo.models.enums.FuelType;
import com.example.demo.models.enums.TransmissionType;
import com.example.demo.models.enums.VehicleStatus;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSearchCriteriaDTO {

	private String brand;
	private String mode;
	private Integer minYear;
	private Integer maxYear;
	private FuelType fuelType;
	private TransmissionType transmissionType;
	private VehicleStatus status;
	private ConditionStatus conditionStatus;
	private Integer minMileage;
	private Integer maxMileage;
	private BigDecimal minDailyPrice;
	private BigDecimal maxDailyPrice;
	private Boolean promotionActive;
	private Integer seatCapacity;
	private String agencyId;
	private String categoryId;
}
