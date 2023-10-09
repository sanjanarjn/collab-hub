package com.nasa.collab.hub.github.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubEntity {

    private String id;

    @JsonProperty("html_url")
    private String htmlUrl;
}
