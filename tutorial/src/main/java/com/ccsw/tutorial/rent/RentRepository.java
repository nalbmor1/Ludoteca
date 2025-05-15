package com.ccsw.tutorial.rent;

import com.ccsw.tutorial.rent.model.Rent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

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

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rent r " + "WHERE r.game.id = :gameId " + "AND r.id <> COALESCE(:rentId, 0) " + "AND r.startDate <= :endDate " + "AND r.endDate >= :startDate")
    boolean existsByGameIdAndDateOverlap(@Param("gameId") Long gameId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("rentId") Long rentId);
}
