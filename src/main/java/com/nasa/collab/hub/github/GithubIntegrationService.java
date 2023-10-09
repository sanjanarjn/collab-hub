package com.nasa.collab.hub.github;

import com.nasa.collab.hub.github.sdk.GithubIssue;
import com.nasa.collab.hub.github.sdk.GithubRepository;
import com.nasa.collab.hub.github.sdk.GithubRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nasa.collab.hub.security.JwtTokenService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GithubIntegrationService {

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private GithubRestClient githubRestClient;

    public List<GithubRepository> searchForRepositories(Map<String, String> queryParams) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", tokenService.generateJwtToken());

        return githubRestClient.searchRepositories(headers, queryParams);
    }

    public List<GithubIssue> searchForIssues(Map<String, String> queryParams) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", tokenService.generateJwtToken());

        return githubRestClient.searchIssues(headers, queryParams);
    }
}
