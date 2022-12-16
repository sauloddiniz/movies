package com.movies.client.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigsFeign {
    @Bean
    public Logger.Level feignLogger(){
        return Logger.Level.FULL;
    }

    @Bean
    public FeignErrorDecode getFeignErrorDecode(){
        return new FeignErrorDecode();
    }
}
