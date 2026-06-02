package com.example.demo.cars.dto;

import com.example.demo.models.enums.ConditionStatus;
import com.example.demo.models.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarStatusUpdateDTO {

	private VehicleStatus currentStatus;
	private ConditionStatus conditionStatus;
}
