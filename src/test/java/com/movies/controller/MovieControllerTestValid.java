package com.movies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.dto.ArtistDTO;
import com.movies.model.dto.MovieDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class MovieControllerTestValid{

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();
    private final String PATH = "src/test/resources/json-files/";

    @SneakyThrows
    @Test
    void whenTrySaveMovieWithoutName() {
        MovieDTO request = mapper.readValue(new File(PATH.concat("MovieDtoToSave.json5")), MovieDTO.class);
        request.setName("");

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenTrySaveMovieWithoutGenre() {
        MovieDTO request = mapper.readValue(new File(PATH.concat("MovieDtoToSave.json5")), MovieDTO.class);
        request.setGenre(List.of());

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void whenTrySaveMovieWithoutCostProduction() {
        MovieDTO request = mapper.readValue(new File(PATH.concat("MovieDtoToSave.json5")), MovieDTO.class);
        request.setCostProduction(BigDecimal.ZERO);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
