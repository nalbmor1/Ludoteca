package com.ccsw.tutorial.rent.model;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

import java.time.LocalDate;

/**
 * @author ccsw
 *
 */
public class RentDto {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private GameDto game;

    private ClientDto client;

    /**
     * @return id
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * @return start date
     */
    public LocalDate getStartDate() {

        return this.startDate;
    }

    /**
     * @param startDate new value of {@link #getStartDate()}.
     */
    public void setStartDate(LocalDate startDate) {

        this.startDate = startDate;
    }

    /**
     * @return end date
     */
    public LocalDate getEndDate() {

        return this.endDate;
    }

    /**
     * @param endDate new value of {@link #getEndDate()}.
     */
    public void setEndDate(LocalDate endDate) {

        this.endDate = endDate;
    }

    /**
     * @return game
     */
    public GameDto getGame() {

        return this.game;
    }

    /**
     * @param game new value of {@link #getGame()}.
     */
    public void setGame(GameDto game) {

        this.game = game;
    }

    /**
     * @return client
     */
    public ClientDto getClient() {

        return this.client;
    }

    /**
     * @param client new value of {@link #getClient()}.
     */
    public void setClient(ClientDto client) {

        this.client = client;
    }
}
