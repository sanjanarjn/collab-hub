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
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RedisCacheService implements CacheService {

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
            this.jedisPool.setex(userId + "_repos", redisConnectionConfiguration.getDefaultTTLInSeconds(), objectMapper.writeValueAsString(repositories));
        } catch (JsonProcessingException e) {
            log.warn("Error occured while serializing data before caching: {}", e.getMessage());
        }
    }

    @Override
    public Optional<List<GithubRepository>> getRepositoriesForUserFromCache(long userId) {
        Optional<List<GithubRepository>> repositories = Optional.empty();
        String repositoriesCached = this.jedisPool.get(userId + "_repos");
        try {
            repositories = repositoriesCached != null ? Optional.of(objectMapper.readValue(repositoriesCached, new TypeReference<List<GithubRepository>>() {
            })) : repositories;
        } catch (JsonProcessingException e) {
            log.warn("Error occured while deserializing data from cache: {}", e.getMessage());
        }
        return repositories;
    }


    @Override
    public void putIssuesForUserInCache(long userId, List<GithubIssue> issues) {
        try {
            this.jedisPool.setex(userId + "_issues", redisConnectionConfiguration.getDefaultTTLInSeconds(), objectMapper.writeValueAsString(issues));
        } catch (JsonProcessingException e) {
            log.warn("Error occured while serializing data before caching: {}", e.getMessage());
        }
    }

    @Override
    public Optional<List<GithubIssue>> getIssuesForUserFromCache(long userId) {
        Optional<List<GithubIssue>> issues = Optional.empty();
        String repositoriesCached = this.jedisPool.get(userId + "_issues");
        try {
            issues = repositoriesCached != null ? Optional.of(objectMapper.readValue(repositoriesCached, new TypeReference<List<GithubIssue>>() {
            })) : issues;
        } catch (JsonProcessingException e) {
            log.warn("Error occured while deserializing data from cache: {}", e.getMessage());
        }
        return issues;
    }
}
