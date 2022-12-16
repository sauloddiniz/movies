package com.movies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.client.ArtistClient;
import com.movies.config.exception.ObjectPresentException;
import com.movies.model.DTO.ArtistDTO;
import com.movies.model.DTO.MovieDTO;
import com.movies.model.Movie;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.event.annotation.AfterTestClass;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieServiceTest{

    @Autowired
    private MovieService movieService;
    @MockBean
    private ArtistClient artistClient;

    private static MovieDTO movieDto;
    private static ArtistDTO artistDto;

    @SneakyThrows
    @BeforeAll()
    public static void init(){

        String path = "src/test/resources/json-files/";
        ObjectMapper mapper = new ObjectMapper();

        movieDto = mapper.readValue(
                new File(path.concat("MovieDtoToSave.json5")), MovieDTO.class);
        artistDto = mapper.readValue(
                new File(path.concat("ArtistDtoToSave.json5")), ArtistDTO.class);
    }

    @Test
    @Order(1)
    public void saveMovie() {

        when(artistClient.findArtistsByNameAndSubname(anyString(), anyString())).thenReturn(artistDto);

        Movie movie = movieService.saveMovie(movieDto);

        Assertions.assertNotNull(movie.getMovieId());
    }

    @Test
    @Order(2)
    public void saveMovieException() {

        when(artistClient.findArtistsByNameAndSubname(anyString(), anyString())).thenReturn(artistDto);

        ObjectPresentException thrown = assertThrows(ObjectPresentException.class, ()-> movieService.saveMovie(movieDto));

        Assertions.assertEquals("Movie already exist: " + movieDto.getName(), thrown.getMessage());
    }

}