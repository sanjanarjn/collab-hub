package com.nasa.collab.hub.github.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasa.collab.hub.config.GithubRestClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GithubRestClient implements GithubRestInterface {

    @Autowired
    private GithubRestClientConfiguration restClientConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<GithubRepository> searchRepositories(Map<String, String> headers, Map<String, String> queryParams) throws JsonProcessingException {
        UriComponentsBuilder searchRepoUriBuilder = UriComponentsBuilder.newInstance()
                .scheme(restClientConfig.getProtocol())
                .host(restClientConfig.getBaseUrl())
                .path(restClientConfig.getSearchRepoUrl());

        for(String key : queryParams.keySet()) {
            searchRepoUriBuilder.queryParam(key, queryParams.get(key));
        }
        URI searchRepoUri = searchRepoUriBuilder.build().toUri();

        log.info("Repo_Search_URI={}", searchRepoUri);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubSearchResponse<GithubRepository>> response = restTemplate.exchange(searchRepoUri, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        GithubSearchResponse<GithubRepository> repositoryResponse = response.getBody();
        return repositoryResponse.getItems();
    }

    @Override
    public List<GithubIssue> searchIssues(Map<String, String> headers, Map<String, String> queryParams) {
        UriComponentsBuilder searchRepoUriBuilder = UriComponentsBuilder.newInstance()
                .scheme(restClientConfig.getProtocol())
                .host(restClientConfig.getBaseUrl())
                .path(restClientConfig.getSearchIssuesUrl());

        for(String key : queryParams.keySet()) {
            searchRepoUriBuilder.queryParam(key, queryParams.get(key));
        }
        URI searchRepoUri = searchRepoUriBuilder.build().toUri();

        log.info("Issue_Search_URI={}", searchRepoUri);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubSearchResponse<GithubIssue>> response = restTemplate.exchange(searchRepoUri, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        GithubSearchResponse<GithubIssue> issueGithubSearchResponse = response.getBody();
        return issueGithubSearchResponse.getItems();
    }
}
