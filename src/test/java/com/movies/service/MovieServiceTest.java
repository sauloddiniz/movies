package com.movies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.client.ArtistClient;
import com.movies.exception.ObjectPresentException;
import com.movies.model.DTO.ArtistDTO;
import com.movies.model.DTO.MovieDTO;
import com.movies.model.Movie;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    private String id;


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
    public void saveMovieOnDbIdoNonNull() {

        when(artistClient.findArtistsByNameAndSubname(anyString(), anyString())).thenReturn(artistDto);

        Movie movie = movieService.saveMovie(movieDto);

        Assertions.assertNotNull(movie.getMovieId());
    }

    @Test
    @Order(2)
    public void trySaveMovieAndThrowObjetExist() {

        when(artistClient.findArtistsByNameAndSubname(anyString(), anyString())).thenReturn(artistDto);

        ObjectPresentException thrown = assertThrows(ObjectPresentException.class, ()-> movieService.saveMovie(movieDto));

        Assertions.assertEquals("Movie already exist: " + movieDto.getName(), thrown.getMessage());
    }

}