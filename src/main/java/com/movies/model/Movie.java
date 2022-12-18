package com.movies.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.movies.model.dto.ArtistDTO;
import com.movies.model.dto.MovieDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDBTable(tableName = "movies")
public class Movie implements Serializable {

    @DynamoDBHashKey(attributeName = "movieId")
    @DynamoDBAttribute
    @DynamoDBAutoGeneratedKey
    private UUID movieId;
    @DynamoDBAttribute
    private String name;
    @DynamoDBAttribute
    private List<String> genre;
    @DynamoDBAttribute
    private Date releaseDate;
    @DynamoDBAttribute
    private BigDecimal costProduction;
    @DynamoDBAttribute
    private List<String> listArtistId;

    @DynamoDBIgnore
    private List<ArtistDTO> listArtist;
    public static Movie converter(MovieDTO movie){
       return Movie.builder()
               .name(movie.getName())
               .genre(movie.getGenre())
               .releaseDate(movie.getReleaseDate())
               .costProduction(movie.getCostProduction())
               .listArtist(movie.getListArtist())
               .listArtistId(movie.getListArtistId())
               .build();
    }

}
