package com.ccsw.tutorial.rent;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.rent.model.Rent;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class RentSpecification implements Specification<Rent> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public RentSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Rent> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<String> path = getPath(root);
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase(">=") && criteria.getValue() != null) {
            return builder.greaterThanOrEqualTo(path.as(LocalDate.class), (LocalDate) criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("<=") && criteria.getValue() != null) {
            return builder.lessThanOrEqualTo(path.as(LocalDate.class), (LocalDate) criteria.getValue());
        }

        return null;
    }

    private Path<String> getPath(Root<Rent> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}
