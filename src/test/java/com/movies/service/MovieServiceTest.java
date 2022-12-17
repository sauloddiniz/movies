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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

        Mockito.when(moviesRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(artistClient.findArtistsByNameAndSubname(Mockito.anyString(),Mockito.anyString())).thenReturn(artistDTO);
        Mockito.when(moviesRepository.save(Mockito.any(Movie.class))).thenReturn(movieSaved);

        Movie movie = movieService.saveMovie(movieNotSaved);

        assertNotNull(movie.getMovieId());
    }

    @SneakyThrows
    @Test
    void whenSaveThenReturnThrowObjectPresent() {

        Movie movie = mapper.readValue(
                new File(path.concat("MovieSaved.json5")), Movie.class);
        String message = "Movie already exist: Jo Jo" ;

        Mockito.when(moviesRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(movie));

        ObjectPresentException thrown = assertThrows(ObjectPresentException.class, () -> movieService.saveMovie(movie));

        assertEquals(message,thrown.getMessage());
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

        UUID id = UUID.fromString("baaaa3b9-6e39-40ff-af42-d3eed2729357");
        String message = "Movie not exist: baaaa3b9-6e39-40ff-af42-d3eed2729357";
        Movie movieNotSaved = mapper.readValue(
                new File(path.concat("MovieSaved.json5")), Movie.class);

        Mockito.when(moviesRepository.findById(movieNotSaved.getMovieId())).thenReturn(Optional.empty());

        ObjectNotFoundException thrown = assertThrows(
                ObjectNotFoundException.class, () -> movieService.findById(id));

        assertEquals(message,thrown.getMessage());
    }
}