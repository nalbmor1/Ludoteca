package com.ccsw.tutorial.rent;

import com.ccsw.tutorial.rent.model.Rent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccsw
 *
 */
public interface RentRepository extends CrudRepository<Rent, Long>, JpaSpecificationExecutor<Rent> {

    /**
     * Método para recuperar un listado paginado de {@link Rent}
     *
     * @param pageable pageable
     * @return {@link Page} de {@link Rent}
     */
    @Override
    @EntityGraph(attributePaths = { "game", "client" })
    Page<Rent> findAll(Specification<Rent> spec, Pageable pageable);
}
