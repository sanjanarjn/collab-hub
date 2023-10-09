package com.nasa.collab.hub.github.sdk;

import lombok.Data;

import java.util.List;

@Data
public class GithubSearchResponse<T extends GithubEntity> {

    private long totalCount;
    private boolean incompleteResults;

    private List<T> items;
}
