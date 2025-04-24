package com.ccsw.tutorial.rent;

import com.ccsw.tutorial.rent.model.Rent;
import com.ccsw.tutorial.rent.model.RentDto;
import com.ccsw.tutorial.rent.model.RentSearchDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

/**
 * @author ccsw
 *
 */
public interface RentService {

    /**
     * Método para recuperar un listado paginado de {@link Rent}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link Rent}
     */
    Page<Rent> findPage(RentSearchDto dto, Long client, Long game, LocalDate selectedDate);

    /**
     * Método para crear o actualizar un {@link Rent}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, RentDto dto) throws Exception;

    /**
     * Método para crear o actualizar un {@link Rent}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

}
