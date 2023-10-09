package com.nasa.collab.hub.github.sdk;

import lombok.Data;

import java.util.List;

@Data
public class GithubIssue extends GithubEntity {

    private String title;
    private String state;
    private List<GithubIssueLabel> labels;
}
