package com.ccsw.tutorial.rent;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.rent.model.Rent;
import com.ccsw.tutorial.rent.model.RentDto;
import com.ccsw.tutorial.rent.model.RentSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class RentServiceImpl implements RentService {

    @Autowired
    RentRepository rentRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    GameService gameService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Rent> findPage(RentSearchDto dto, Long client, Long game, LocalDate selectedDate) {

        RentSpecification clientSpec = new RentSpecification(new SearchCriteria("client.id", ":", client));
        RentSpecification gameSpec = new RentSpecification(new SearchCriteria("game.id", ":", game));
        RentSpecification dateStartSpec = new RentSpecification(new SearchCriteria("endDate", ">=", selectedDate));
        RentSpecification dateEndSpec = new RentSpecification(new SearchCriteria("startDate", "<=", selectedDate));

        Specification<Rent> spec = Specification.where(clientSpec).and(gameSpec).and(dateStartSpec).and(dateEndSpec);

        return this.rentRepository.findAll(spec, dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, RentDto data) throws Exception {

        Rent rent;

        if (id == null) {
            rent = new Rent();
        } else {
            rent = this.get(id);
        }

        BeanUtils.copyProperties(data, rent, "id", "client", "game");

        rent.setClient(clientService.get(data.getClient().getId()));
        rent.setGame(gameService.get(data.getGame().getId()));

        if (rent.getStartDate().isAfter(rent.getEndDate())) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        long days = java.time.temporal.ChronoUnit.DAYS.between(rent.getStartDate(), rent.getEndDate());
        if (days > 14) {
            throw new Exception("El préstamo no puede ser mayor a 14 días");
        }

        boolean overlapping = rentRepository.existsByGameIdAndDateOverlap(rent.getGame().getId(), rent.getStartDate(), rent.getEndDate(), id);

        if (overlapping) {
            throw new Exception("El juego ya está prestado a otro cliente en esas fechas.");
        }

        long overlappingRentsByClient = rentRepository.countOverlappingRentsByClient(rent.getClient().getId(), rent.getStartDate(), rent.getEndDate(), id);

        if (overlappingRentsByClient >= 2) {
            throw new Exception("Un cliente no puede tener más de dos juegos prestados al mismo tiempo.");
        }

        this.rentRepository.save(rent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.rentRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        }

        this.rentRepository.deleteById(id);
    }

    public Rent get(Long id) {

        return this.rentRepository.findById(id).orElse(null);
    }

}
