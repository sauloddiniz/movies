package com.movies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.model.Movie;
import com.movies.model.dto.MovieDTO;
import com.movies.service.MovieService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MoviesControllerTest {

    @InjectMocks
    private MoviesController moviesController;
    @Mock
    private MovieService movieService;
    private ObjectMapper mapper = new ObjectMapper();
    private final String PATH = "src/test/resources/json-files/";

    @SneakyThrows
    @Test
    void whenCallFindAllMoviesThenReturnListObject() {

        List<Movie> listMovies = Arrays.asList(mapper.readValue(
                new File(PATH.concat("ListOfMovies.json5")), Movie[].class));

        Mockito.when(movieService.findAll()).thenReturn(listMovies);

        ResponseEntity<List<MovieDTO>> response = moviesController.findAllMovies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listMovies.size(), response.getBody().size());
        assertEquals(MovieDTO.class, response.getBody().get(0).getClass());
    }

    @SneakyThrows
    @Test
    void whenCallFindAllMoviesThenReturnListEmptyObject() {

        List<Movie> listMovies = Arrays.asList(mapper.readValue(
                new File(PATH.concat("ListEmpty.json5")), Movie[].class));

        Mockito.when(movieService.findAll()).thenReturn(listMovies);

        ResponseEntity<List<MovieDTO>> response = moviesController.findAllMovies();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @SneakyThrows
    @Test
    void whenSaveThenSuccess() {

        MovieDTO request = mapper.readValue(new File(PATH.concat("MovieDtoToSave.json5")), MovieDTO.class);
        Movie requestSave = mapper.readValue(new File(PATH.concat("MovieSaved.json5")), Movie.class);

        Mockito.when(movieService.saveMovie(Mockito.any())).thenReturn(requestSave);

        ResponseEntity<MovieDTO> response = moviesController.saveMovie(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders().getLocation());
    }

    @SneakyThrows
    @Test
    void whenCallFindByIdThenReturnSuccess() {

        Movie requestSave = mapper.readValue(new File(PATH.concat("MovieSaved.json5")), Movie.class);
        String id = "baaaa3b9-6e39-40ff-af42-d3eed2729357";
        Mockito.when(movieService.findById(Mockito.any())).thenReturn(requestSave);

        ResponseEntity<MovieDTO> response = moviesController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getMovieId());
    }

}