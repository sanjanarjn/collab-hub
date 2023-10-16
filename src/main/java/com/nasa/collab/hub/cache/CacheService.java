package com.nasa.collab.hub.cache;

import com.nasa.collab.hub.github.sdk.GithubIssue;
import com.nasa.collab.hub.github.sdk.GithubRepository;

import java.util.List;
import java.util.Optional;

public interface CacheService {

    public void putRepositoriesForUserInCache(long userId, List<GithubRepository> repositories);

    public Optional<List<GithubRepository>> getRepositoriesForUserFromCache(long userId);

    public void putIssuesForUserInCache(long userId, List<GithubIssue> repositories);

    public Optional<List<GithubIssue>> getIssuesForUserFromCache(long userId);
}
