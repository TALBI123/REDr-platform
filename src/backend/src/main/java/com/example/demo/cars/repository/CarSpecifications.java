package com.example.demo.cars.repository;

import com.example.demo.cars.dto.CarSearchCriteriaDTO;
import com.example.demo.models.agency.Car;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class CarSpecifications {

    private CarSpecifications() {
    }

    public static Specification<Car> from(CarSearchCriteriaDTO criteria) {
        return (root, query, builder) -> {
            if (criteria == null) {
                return builder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getBrand())) {
                predicates.add(builder.like(
                        builder.lower(root.get("brand")),
                        "%" + criteria.getBrand().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getMode())) {
                predicates.add(builder.like(
                        builder.lower(root.get("mode")),
                        "%" + criteria.getMode().toLowerCase() + "%"));
            }

            if (criteria.getMinYear() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("year"), criteria.getMinYear()));
            }

            if (criteria.getMaxYear() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("year"), criteria.getMaxYear()));
            }

            if (criteria.getFuelType() != null) {
                predicates.add(builder.isMember(criteria.getFuelType(), root.get("fuelTypes")));
            }

            if (criteria.getTransmissionType() != null) {
                predicates.add(builder.equal(root.get("transmissionType"), criteria.getTransmissionType()));
            }

            if (criteria.getStatus() != null) {
                predicates.add(builder.equal(root.get("currentStatus"), criteria.getStatus()));
            }

            if (criteria.getConditionStatus() != null) {
                predicates.add(builder.equal(root.get("conditionStatus"), criteria.getConditionStatus()));
            }

            if (criteria.getMinMileage() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("mileage"), criteria.getMinMileage()));
            }

            if (criteria.getMaxMileage() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("mileage"), criteria.getMaxMileage()));
            }

            if (criteria.getMinDailyPrice() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dailyPrice"), criteria.getMinDailyPrice()));
            }

            if (criteria.getMaxDailyPrice() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dailyPrice"), criteria.getMaxDailyPrice()));
            }

            if (criteria.getPromotionActive() != null) {
                predicates.add(builder.equal(root.get("promotionActive"), criteria.getPromotionActive()));
            }

            if (criteria.getSeatCapacity() != null) {
                predicates.add(builder.equal(root.get("seatCapacity"), criteria.getSeatCapacity()));
            }

            if (StringUtils.hasText(criteria.getAgencyId())) {
                predicates.add(builder.equal(root.get("agency").get("id"), criteria.getAgencyId()));
            }

            if (StringUtils.hasText(criteria.getCategoryId())) {
                predicates.add(builder.equal(root.get("category").get("id"), criteria.getCategoryId()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
