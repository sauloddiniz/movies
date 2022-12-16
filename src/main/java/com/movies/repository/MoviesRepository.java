package com.movies.repository;

import com.movies.model.Movie;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@EnableScan
public interface MoviesRepository extends CrudRepository<Movie, UUID> {
    List<Movie> findAll();
    Optional<Movie> findByName(String name);
}
