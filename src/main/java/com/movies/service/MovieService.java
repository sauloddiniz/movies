package com.movies.service;

import com.movies.client.ArtistClient;
import com.movies.exception.UpdateConflictException;
import com.movies.exception.ObjectNotFoundException;
import com.movies.exception.ObjectPresentException;
import com.movies.model.dto.ArtistDTO;
import com.movies.model.Movie;
import com.movies.repository.MoviesRepository;
import com.newrelic.api.agent.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.newrelic.api.agent.NewRelic.addCustomParameters;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final ArtistClient artistClientService;
    private final MoviesRepository moviesRepository;

    @Trace
    public Movie saveMovie(Movie movie) {
        alreadyExistMovie(movie);
        List<ArtistDTO> listArtist = movie.getListArtist()
                .stream()
                .map(e -> artistClientService.findArtistsByNameAndSubname(e.getName(), e.getSubName()))
                .collect(Collectors.toList());
        movie.setListArtistId(listArtist.stream().map(ArtistDTO::getArtistId).collect(Collectors.toList()));
        Movie movieSaved = moviesRepository.save(movie);
        addCustomParameters(Map.of("movieId", Optional.ofNullable(movieSaved.getMovieId().toString()), "movieName", movieSaved.getName()));
        return movieSaved;
    }

    public void alreadyExistMovie(Movie movie) {
        moviesRepository.findByName(movie.getName())
                .ifPresent(e -> {throw new ObjectPresentException("Movie already exist");});
    }

    public Movie findById(UUID id){
        return moviesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Movie not exist"));
    }

    public List<Movie> findAll() {
        return moviesRepository.findAll();
    }

    public void deleteById(UUID id) {moviesRepository.deleteById(id);}

    public Movie update(UUID id, Movie movie) {
        verifyObject(id, movie);
        moviesRepository.save(movie);
        return movie;
    }

    private void verifyObject(UUID id, Movie movie) {
        moviesRepository.findById(id).ifPresentOrElse(
                e -> {  if (e.getMovieId() != (movie.getMovieId())) {
                    throw new UpdateConflictException("Object is different");
                }
                    },
                () -> {
                    throw new ObjectNotFoundException("Movie not exist");
                });
    }
}
