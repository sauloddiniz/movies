package com.movies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.client.ArtistClient;
import com.movies.exception.ObjectNotFoundException;
import com.movies.exception.ObjectPresentException;
import com.movies.model.DTO.ArtistDTO;
import com.movies.model.Movie;
import com.movies.repository.MoviesRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MoviesRepository moviesRepository;

    @Mock
    private ArtistClient artistClient;
    private final String path = "src/test/resources/json-files/";;
    ObjectMapper mapper = new ObjectMapper();


    @SneakyThrows
    @Test
    void whenSaveThenReturnObjectWithId() {

        ArtistDTO  artistDTO = mapper.readValue(
                new File(path.concat("ArtistDtoToSave.json5")), ArtistDTO.class);
        Movie movieNotSaved = mapper.readValue(
                new File(path.concat("MovieNotSaved.json5")), Movie.class);
        Movie movieSaved = mapper.readValue(
                new File(path.concat("MovieSaved.json5")), Movie.class);

        Mockito.when(moviesRepository.findByName(anyString())).thenReturn(Optional.empty());
        Mockito.when(artistClient.findArtistsByNameAndSubname(anyString(), anyString())).thenReturn(artistDTO);
        Mockito.when(moviesRepository.save(any(Movie.class))).thenReturn(movieSaved);

        Movie movie = movieService.saveMovie(movieNotSaved);

        assertNotNull(movie.getMovieId());
    }

    @SneakyThrows
    @Test
    void whenSaveThenReturnThrowObjectPresent() {

        Movie movie = mapper.readValue(
                new File(path.concat("MovieSaved.json5")), Movie.class);
        String message = "Movie already exist: Jo Jo" ;

        Mockito.when(moviesRepository.findByName(anyString())).thenReturn(Optional.of(movie));

        try {
            movieService.saveMovie(movie);
        }catch (Exception e){
            assertEquals(ObjectPresentException.class, e.getClass());
            assertEquals(message,e.getMessage());
        }

    }

    @SneakyThrows
    @Test
    void whenFindByIdThenReturnObject() {

        UUID id = UUID.fromString("baaaa3b9-6e39-40ff-af42-d3eed2729357");
        Movie movie = mapper.readValue(
                new File(path.concat("MovieSaved.json5")), Movie.class);

        Mockito.when(moviesRepository.findById(movie.getMovieId())).thenReturn(Optional.of(movie));

        Movie response = movieService.findById(movie.getMovieId());

        assertNotNull(response);
        assertEquals(Movie.class, response.getClass());
        assertEquals(id, response.getMovieId());
    }

    @SneakyThrows
    @Test
    void whenFindByIdThenThrowObjectNotFoundException() {

        UUID id = UUID.randomUUID();
        String message = "Movie not exist: " +id;

        Mockito.when(moviesRepository.findById(id)).thenThrow(new ObjectNotFoundException("Movie not exist: " +id));

        try {
            movieService.findById(id);
        } catch (Exception e){
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(message, e.getMessage());
        }

    }

    @SneakyThrows
    @Test
    void whenFindAllThenReturnListSize() {

        Integer listSize = 2;
        List<Movie> listMovies = Arrays.asList(mapper.readValue(
                new File(path.concat("ListOfMovies.json5")), Movie[].class));

        Mockito.when(moviesRepository.findAll()).thenReturn(listMovies);

        List<Movie> movies = movieService.findAll();

        assertNotNull(movies);
        assertEquals(Movie.class, movies.get(0).getClass());
        assertEquals(listSize,movies.size());
    }

    @SneakyThrows
    @Test
    void whenFindAllThenReturnListEmpty() {

        List<Movie> listMoviesEmpty = Arrays.asList(mapper.readValue(
                new File(path.concat("ListEmpty.json5")), Movie[].class));

        Mockito.when(moviesRepository.findAll()).thenReturn(listMoviesEmpty);

        List<Movie> movies = movieService.findAll();

        assertEquals(List.of(), movies);
        assertEquals(0,movies.size());
    }
}