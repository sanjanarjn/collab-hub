package com.nasa.collab.hub.github.sdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "github.rest")
public class GithubRestClientConfig {

    private String protocol;
    private String baseUrl;
    private String searchRepoUrl;
    private String searchIssuesUrl;
}
