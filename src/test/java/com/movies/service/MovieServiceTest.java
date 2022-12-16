package com.movies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.client.ArtistClient;
import com.movies.exception.ObjectNotFoundException;
import com.movies.exception.ObjectPresentException;
import com.movies.model.DTO.ArtistDTO;
import com.movies.model.DTO.MovieDTO;
import com.movies.model.Movie;
import com.movies.repository.MoviesRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MoviesRepository moviesRepository;

    @Mock
    private ArtistClient artistClient;



    @SneakyThrows
    @Test
    void whenSaveThenReturnObjectWithId() {
        String path = "src/test/resources/json-files/";
        ObjectMapper mapper = new ObjectMapper();

        MovieDTO movieDTO = mapper.readValue(
                new File(path.concat("MovieDtoToSave.json5")), MovieDTO.class);
        ArtistDTO  artistDTO = mapper.readValue(
                new File(path.concat("ArtistDtoToSave.json5")), ArtistDTO.class);
        Movie movie = mapper.readValue(
                new File(path.concat("MovieSave.json5")), Movie.class);

        Mockito.when(moviesRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(artistClient.findArtistsByNameAndSubname(Mockito.anyString(),Mockito.anyString())).thenReturn(artistDTO);
        Mockito.when(moviesRepository.save(Mockito.any(Movie.class))).thenReturn(movie);

        Movie aux = movieService.saveMovie(movieDTO);

        Assertions.assertNotNull(aux.getMovieId());
    }

    @SneakyThrows
    @Test
    void whenSaveThenReturnThrowObjectPresent() {
        String path = "src/test/resources/json-files/";
        ObjectMapper mapper = new ObjectMapper();

        Movie movie = mapper.readValue(
                new File(path.concat("MovieSave.json5")), Movie.class);
        MovieDTO movieDTO = mapper.readValue(
                new File(path.concat("MovieDtoToSave.json5")), MovieDTO.class);
        String message = "Movie already exist: " + movie.getName();

        Mockito.when(moviesRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(movie));

        ObjectPresentException thrown = assertThrows(ObjectPresentException.class, () -> movieService.saveMovie(movieDTO));

        assertEquals(message,thrown.getMessage());
    }

    @Test
    void alreadyExistMovie() {
    }

    @SneakyThrows
    @Test
    void whenFindByIdThenReturnObject() {
        String path = "src/test/resources/json-files/";
        ObjectMapper mapper = new ObjectMapper();

        Movie movie = mapper.readValue(
                new File(path.concat("MovieSave.json5")), Movie.class);
        String message = "Movie not exist: baaaa3b9-6e39-40ff-af42-d3eed2729357";

        Mockito.when(moviesRepository.findById(movie.getMovieId())).thenReturn(Optional.of(movie));

        assertDoesNotThrow(() -> movieService.findById(movie.getMovieId().toString()));
    }

    @SneakyThrows
    @Test
    void whenFindByIdThenThrowObjectNotFoundException() {
        String path = "src/test/resources/json-files/";
        ObjectMapper mapper = new ObjectMapper();

        Movie movie = mapper.readValue(
                new File(path.concat("MovieSave.json5")), Movie.class);
        String message = "Movie not exist: baaaa3b9-6e39-40ff-af42-d3eed2729357";

        Mockito.when(moviesRepository.findById(movie.getMovieId())).thenReturn(Optional.empty());

        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> movieService.findById(movie.getMovieId().toString()));

        assertEquals(message,thrown.getMessage());
    }


    private void constructorObjects(){

    }
}