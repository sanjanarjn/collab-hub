package com.nasa.collab.hub.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasa.collab.hub.config.RedisConnectionConfiguration;
import com.nasa.collab.hub.github.sdk.GithubIssue;
import com.nasa.collab.hub.github.sdk.GithubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisCacheService implements CacheService {

    public static final String REPOS_CACHE_KEY_SUFFIX = "_repos";
    private static final String ISSUES_CACHE_KEY_SUFFIX = "_issues";

    private RedisConnectionConfiguration redisConnectionConfiguration;
    private JedisPooled jedisPool;

    @Autowired
    private ObjectMapper objectMapper;

    public RedisCacheService(@Autowired RedisConnectionConfiguration redisConnectionConfiguration) {
        this.redisConnectionConfiguration = redisConnectionConfiguration;
        this.jedisPool = new JedisPooled(redisConnectionConfiguration.getHost(), redisConnectionConfiguration.getPort());
    }

    @Override
    public void putRepositoriesForUserInCache(long userId, List<GithubRepository> repositories) {
        try {
            for (GithubRepository githubRepository : repositories) {
                String repoId = githubRepository.getId();
                this.jedisPool.lpush(userId + REPOS_CACHE_KEY_SUFFIX, repoId);
                this.jedisPool.expire(userId + REPOS_CACHE_KEY_SUFFIX, redisConnectionConfiguration.getDefaultTTLInSeconds());
                this.jedisPool.setex(repoId, redisConnectionConfiguration.getDefaultTTLInSeconds(), objectMapper.writeValueAsString(githubRepository));
            }
        } catch (JsonProcessingException e) {
            log.warn("Error occured while serializing data before caching: {}", e.getMessage());
        }
    }

    @Override
    public Optional<List<GithubRepository>> getRepositoriesForUserFromCache(long userId) {
        List<String> repoIdsCached = this.jedisPool.lrange(userId + REPOS_CACHE_KEY_SUFFIX, 0, -1);
        if (CollectionUtils.isEmpty(repoIdsCached))
            return Optional.empty();

        List<GithubRepository> repositories = new ArrayList<>();
        try {
            for (String repoId : repoIdsCached) {
                repositories.add(objectMapper.readValue(this.jedisPool.get(repoId), GithubRepository.class));
            }
        } catch (JsonProcessingException e) {
            log.warn("Error occured while deserializing data from cache: {}", e.getMessage());
        }
        return Optional.of(repositories);
    }


    @Override
    public void putIssuesForUserInCache(long userId, List<GithubIssue> issues) {
        try {
            for (GithubIssue githubIssue : issues) {
                String issueId = githubIssue.getId();
                this.jedisPool.lpush(userId + ISSUES_CACHE_KEY_SUFFIX, issueId);
                this.jedisPool.expire(userId + ISSUES_CACHE_KEY_SUFFIX, redisConnectionConfiguration.getDefaultTTLInSeconds());
                this.jedisPool.setex(issueId, redisConnectionConfiguration.getDefaultTTLInSeconds(), objectMapper.writeValueAsString(githubIssue));
            }
        } catch (JsonProcessingException e) {
            log.warn("Error occured while serializing data before caching: {}", e.getMessage());
        }
    }

    @Override
    public Optional<List<GithubIssue>> getIssuesForUserFromCache(long userId) {
        List<String> issueIdsCached = this.jedisPool.lrange(userId + ISSUES_CACHE_KEY_SUFFIX, 0, -1);
        if (CollectionUtils.isEmpty(issueIdsCached))
            return Optional.empty();

        List<GithubIssue> issues = new ArrayList<>();
        try {
            for (String issueId : issueIdsCached) {
                issues.add(objectMapper.readValue(this.jedisPool.get(issueId), GithubIssue.class));
            }
        } catch (JsonProcessingException e) {
            log.warn("Error occured while deserializing data from cache: {}", e.getMessage());
        }
        return Optional.of(issues);
    }
}
