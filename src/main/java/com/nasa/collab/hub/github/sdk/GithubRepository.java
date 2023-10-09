package com.nasa.collab.hub.github.sdk;

import lombok.Data;

@Data
public class GithubRepository extends GithubEntity {

    private String name;
    private String description;
    private String language;
}
