package com.nasa.collab.hub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="redis")
@Data
public class RedisConnectionConfiguration {

    private String host;
    private int port;
    private long defaultTTLInSeconds;
}
