package com.nasa.collab.hub.github.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GithubIssueAction {

    @JsonProperty("opened")
    OPENED,

    @JsonProperty("labeled")
    LABELED
}
