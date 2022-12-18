package com.movies.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.dto.MovieDTO;
import com.movies.model.dto.error.ErrorResponseDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.WebRequest;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class DefaultResponseEntityExceptionHandlerTest {
    @Autowired
    private DefaultResponseEntityExceptionHandler exceptionHandler;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private  MockHttpServletRequest servletRequest;
    private ObjectMapper mapper = new ObjectMapper();
    private final String PATH = "src/test/resources/json-files/";

    @Test
    void whenObjectPresentThenThrowException() {
        String message = "Movie already exist";

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler
                .objectPresentException(new ObjectPresentException(message), servletRequest);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody().getTimestamp());
        Assertions.assertEquals(ErrorResponseDTO.class, response.getBody().getClass());
        Assertions.assertEquals(message, response.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void whenObjectNotFoundThrowException() {
        String message = "Movie not exist";

        ResponseEntity<ErrorResponseDTO> response = exceptionHandler
                .objectNotFoundException(new ObjectNotFoundException(message), servletRequest);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody().getTimestamp());
        Assertions.assertEquals(ErrorResponseDTO.class, response.getBody().getClass());
        Assertions.assertEquals(message, response.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @SneakyThrows
    @Test
    void whenHandleMethodArgumentNameNotValid() {
        MovieDTO request = mapper.readValue(new File(PATH.concat("MovieDtoToSave.json5")), MovieDTO.class);
        request.setName("");

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenHandleMethodArgumentGenreNotValid() {
        MovieDTO request = mapper.readValue(new File(PATH.concat("MovieDtoToSave.json5")), MovieDTO.class);
        request.setGenre(List.of());

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenHandleMethodArgumentCostProductionNotValid() {
        MovieDTO request = mapper.readValue(new File(PATH.concat("MovieDtoToSave.json5")), MovieDTO.class);
        request.setCostProduction(BigDecimal.ZERO);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}