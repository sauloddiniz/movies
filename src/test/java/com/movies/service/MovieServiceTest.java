package com.movies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.client.ArtistClient;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


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

    @Test
    void findById() {
    }


    private void constructorObjects(){

    }
}