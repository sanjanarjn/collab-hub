package com.nasa.collab.hub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPooled;

@Configuration
public class CollabHubConfiguration {

    @Bean
    public ObjectMapper getObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
