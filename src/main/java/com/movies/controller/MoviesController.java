package com.movies.controller;

import com.movies.model.dto.error.ErrorResponseDTO;
import com.movies.model.dto.MovieDTO;
import com.movies.model.Movie;
import com.movies.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("movies")
@Slf4j
@Tag(name = "movies", description = "endpoints for manager movies")
public class MoviesController {

    private final MovieService movieService;

    public MoviesController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Operation(summary = "all movies in db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MovieDTO.class)))}),
            @ApiResponse(responseCode = "204", description = "No Content",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Arrays.class)))})
    })
    public ResponseEntity<List<MovieDTO>> findAllMovies(){
        List<Movie> listMovies = movieService.findAll();
        return listMovies.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(listMovies.stream().map(MovieDTO::converter).collect(Collectors.toList()));
    }

    @PostMapping
    @Operation(summary = "save movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                             schema = @Schema(implementation = MovieDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))})
    })
    public ResponseEntity<MovieDTO> saveMovie(@Valid @RequestBody MovieDTO movieRequest){
        Movie movie = movieService.saveMovie(Movie.converter(movieRequest));
        URI uri = URI.create(movie.getMovieId().toString());
        return ResponseEntity.created(uri).body(MovieDTO.converter(movie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> findById(@PathVariable String id){
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(MovieDTO.converter(movieService.findById(uuid)));
    }
}

