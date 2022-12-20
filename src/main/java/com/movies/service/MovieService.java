package com.movies.service;

import com.movies.client.ArtistClient;
import com.movies.exception.UpdateConflictException;
import com.movies.exception.ObjectNotFoundException;
import com.movies.exception.ObjectPresentException;
import com.movies.model.dto.ArtistDTO;
import com.movies.model.Movie;
import com.movies.repository.MoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final ArtistClient artistClientService;
    private final MoviesRepository moviesRepository;

    public Movie saveMovie(Movie movie) {
        alreadyExistMovie(movie);
        List<ArtistDTO> listArtist = movie.getListArtist()
                .stream()
                .map(e -> artistClientService.findArtistsByNameAndSubname(e.getName(), e.getSubName()))
                .collect(Collectors.toList());
        movie.setListArtistId(listArtist.stream().map(ArtistDTO::getArtistId).collect(Collectors.toList()));
        return moviesRepository.save(movie);
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
