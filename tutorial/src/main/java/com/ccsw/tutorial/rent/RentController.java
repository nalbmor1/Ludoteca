package com.ccsw.tutorial.rent;

import com.ccsw.tutorial.rent.model.Rent;
import com.ccsw.tutorial.rent.model.RentDto;
import com.ccsw.tutorial.rent.model.RentSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Tag(name = "Rent", description = "API of Rent")
@RequestMapping(value = "/rent")
@RestController
@CrossOrigin(origins = "*")
public class RentController {

    @Autowired
    RentService rentService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar un listado paginado de {@link Rent}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link RentDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a page of Rents")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<RentDto> findPage(@RequestBody RentSearchDto dto, @RequestParam(required = false) Long client, @RequestParam(required = false) Long game, @RequestParam(required = false) LocalDate selectedDate) {

        Page<Rent> page = this.rentService.findPage(dto, client, game, selectedDate);

        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, RentDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para crear o actualizar un {@link Rent}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Rent")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody RentDto dto) throws Exception {

        this.rentService.save(id, dto);
    }

    /**
     * Método para crear o actualizar un {@link Rent}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Rent")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.rentService.delete(id);
    }
}
