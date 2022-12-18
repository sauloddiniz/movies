package com.movies.model.dto;

import com.movies.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovieDTO {
    private String movieId;
    @NotBlank
    private String name;
    @Size(min = 1)
    @NotNull
    private List<String> genre;
    @Size(min = 1)
    @NotNull
    private List<ArtistDTO> listArtist;
    @NotNull
    private Date releaseDate;
    @DecimalMin("0.01")
    private BigDecimal costProduction;
    private List<String> listArtistId;

    public static MovieDTO converter(Movie movie){
        return MovieDTO.builder()
                .movieId(movie.getMovieId().toString())
                .name(movie.getName())
                .genre(movie.getGenre())
                .releaseDate(movie.getReleaseDate())
                .costProduction(movie.getCostProduction())
                .listArtist(movie.getListArtist())
                .build();
    }
}
