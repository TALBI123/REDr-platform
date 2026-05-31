package com.example.demo.agency.repository;

import com.example.demo.agency.dto.AgencySearchCriteria;
import com.example.demo.models.agency.Agency;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class AgencySpecifications {

    private AgencySpecifications() {
    }

    public static Specification<Agency> from(AgencySearchCriteria criteria) {
        return (root, query, builder) -> {
            if (criteria == null) {
                return builder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getName())) {
                predicates.add(builder.like(
                        builder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getCity())) {
                predicates.add(builder.like(
                        builder.lower(root.get("city")),
                        "%" + criteria.getCity().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getEmail())) {
                predicates.add(builder.like(
                        builder.lower(root.get("email")),
                        "%" + criteria.getEmail().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getPhone())) {
                predicates.add(builder.like(
                        builder.lower(root.get("phone")),
                        "%" + criteria.getPhone().toLowerCase() + "%"));
            }

            if (criteria.getStatus() != null) {
                predicates.add(builder.equal(root.get("status"), criteria.getStatus()));
            }

            if (criteria.getMinRating() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("rating"), criteria.getMinRating()));
            }

            if (criteria.getMaxRating() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("rating"), criteria.getMaxRating()));
            }

            if (criteria.getApprovalFrom() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("approvalDate"), criteria.getApprovalFrom()));
            }

            if (criteria.getApprovalTo() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("approvalDate"), criteria.getApprovalTo()));
            }

            if (criteria.getInscriptionFrom() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("inscriptionDate"), criteria.getInscriptionFrom()));
            }

            if (criteria.getInscriptionTo() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("inscriptionDate"), criteria.getInscriptionTo()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
