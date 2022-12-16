package com.movies.client;

import com.movies.model.DTO.ArtistDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "artist-api", url = "${api-artist.artist}")
public interface ArtistClient {

    @GetMapping(value = "/artists",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ArtistDTO findArtistsByNameAndSubname(
            @RequestParam("name") String name, @RequestParam("subName") String subName);
}
