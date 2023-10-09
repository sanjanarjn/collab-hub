package com.nasa.collab.hub.github.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface GithubRestInterface {

    public List<GithubRepository> searchRepositories(Map<String, String> headers, Map<String, String> queryParams) throws JsonProcessingException;

    public List<GithubIssue> searchIssues(Map<String, String> headers, Map<String, String> queryParams);

}
