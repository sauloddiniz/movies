package com.movies.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.movies.model.DTO.ArtistDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

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
