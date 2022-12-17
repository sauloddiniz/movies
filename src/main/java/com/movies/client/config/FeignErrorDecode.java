package com.movies.client.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecode implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                if (methodKey.contains("artist-api")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),"not found");
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
