package com.movies.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.movies.model.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistDTO implements Serializable {
    private String artistId;
    @NotBlank
    private String name;
    @NotBlank
    private String subName;
    private Date birthDate;

    public static ArtistDTO converter(Artist artist) {
        return ArtistDTO.builder()
                .name(artist.getName())
                .subName(artist.getSubName())
                .birthDate(artist.getBirthDate())
                .build();
    }
}