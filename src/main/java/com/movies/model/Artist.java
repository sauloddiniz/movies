package com.movies.model;

import com.movies.model.dto.ArtistDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Artist {
    private String name;
    private String subName;
    private Date birthDate;

    public static Artist converter(ArtistDTO artist) {
        return Artist.builder()
                .name(artist.getName().toUpperCase())
                .subName(artist.getSubName().toUpperCase())
                .birthDate(artist.getBirthDate())
                .build();
    }
}
