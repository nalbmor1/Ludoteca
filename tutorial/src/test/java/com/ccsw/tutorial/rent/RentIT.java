package com.ccsw.tutorial.rent;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.rent.model.RentDto;
import com.ccsw.tutorial.rent.model.RentSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RentIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/rent";

    public static final Long DELETE_RENT_ID = 3L;
    public static final Long MODIFY_RENT_ID = 1L;

    public static final LocalDate NEW_START_DATE = LocalDate.parse("2025-05-03");
    public static final LocalDate NEW_END_DATE = LocalDate.parse("2025-05-10");
    public static final Long NEW_CLIENT_ID = 1L;
    public static final Long NEW_GAME_ID = 2L;

    private static final int TOTAL_RENTS = 3;
    private static final int PAGE_SIZE = 2;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<RentDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<RentDto>>() {
    };

    @Test
    public void findFirstPageWithTwoSizeShouldReturnFirstTwoResults() {

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_RENTS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithTwoSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_RENTS - PAGE_SIZE;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_RENTS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewRent() {

        long newRentId = TOTAL_RENTS + 1;
        long newRentSize = TOTAL_RENTS + 1;

        GameDto game = new GameDto();
        game.setId(NEW_GAME_ID);

        ClientDto client = new ClientDto();
        client.setId(NEW_CLIENT_ID);

        RentDto dto = new RentDto();
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);
        dto.setClient(client);
        dto.setGame(game);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newRentSize));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newRentSize, response.getBody().getTotalElements());

        RentDto rent = response.getBody().getContent().stream().filter(item -> item.getId().equals(newRentId)).findFirst().orElse(null);
        assertNotNull(rent);
        assertEquals(NEW_START_DATE, rent.getStartDate());
        assertEquals(NEW_END_DATE, rent.getEndDate());
    }

    @Test
    public void modifyWithExistIdShouldModifyRent() {

        RentDto dto = new RentDto();
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);

        GameDto game = new GameDto();
        game.setId(NEW_GAME_ID);

        ClientDto client = new ClientDto();
        client.setId(NEW_CLIENT_ID);

        dto.setClient(client);
        dto.setGame(game);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_RENT_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_RENTS, response.getBody().getTotalElements());

        RentDto rent = response.getBody().getContent().stream().filter(item -> item.getId().equals(MODIFY_RENT_ID)).findFirst().orElse(null);
        assertNotNull(rent);
        assertEquals(NEW_START_DATE, rent.getStartDate());
        assertEquals(NEW_END_DATE, rent.getEndDate());
    }

    @Test
    public void modifyWithNotExistIdShouldThrowException() {

        long rentId = TOTAL_RENTS + 1;

        RentDto dto = new RentDto();
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);

        GameDto game = new GameDto();
        game.setId(NEW_GAME_ID);

        ClientDto client = new ClientDto();
        client.setId(NEW_CLIENT_ID);

        dto.setClient(client);
        dto.setGame(game);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + rentId, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteRent() {

        long newRentSize = TOTAL_RENTS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_RENT_ID, HttpMethod.DELETE, null, Void.class);

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_RENTS));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newRentSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteRentId = TOTAL_RENTS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteRentId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void findExistsClientShouldReturnRents() {

        int RENTS_WITH_FILTER = 1;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?client=" + 1L, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameShouldReturnRents() {

        int RENTS_WITH_FILTER = 1;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?game=" + 3L, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findBetweenDateShouldReturnRents() {

        int RENTS_WITH_FILTER = 1;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?selectedDate=" + LocalDate.parse("2025-02-11"), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndClientShouldReturnRents() {

        int RENTS_WITH_FILTER = 1;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?game=" + 1L + "&client=" + 2L, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndClientAndDateShouldReturnRents() {

        int RENTS_WITH_FILTER = 1;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?game=" + 1L + "&client=" + 2L + "&selectedDate=" + LocalDate.parse("2025-02-11"), HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsClientShouldReturnEmpty() {

        int RENTS_WITH_FILTER = 0;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?client=" + 4L, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsGameShouldReturnEmpty() {

        int RENTS_WITH_FILTER = 0;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?game=" + 2L, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void findNotBetweenDateShouldReturnEmpty() {

        int RENTS_WITH_FILTER = 0;

        RentSearchDto searchDto = new RentSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<RentDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?selectedDate=" + LocalDate.parse("2024-02-07"), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(RENTS_WITH_FILTER, response.getBody().getTotalElements());
    }

    @Test
    public void saveWithStartDateAfterEndDateShouldThrowException() {

        GameDto game = new GameDto();
        game.setId(NEW_GAME_ID);

        ClientDto client = new ClientDto();
        client.setId(NEW_CLIENT_ID);

        RentDto dto = new RentDto();
        dto.setStartDate(LocalDate.parse("2025-05-03"));
        dto.setEndDate(LocalDate.parse("2025-02-03"));
        dto.setClient(client);
        dto.setGame(game);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + 1L, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
