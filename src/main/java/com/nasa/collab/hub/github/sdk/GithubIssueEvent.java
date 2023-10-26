package com.nasa.collab.hub.github.sdk;

import lombok.Data;

@Data
public class GithubIssueEvent {

    private GithubIssueAction action;
    private GithubIssue issue;
}
