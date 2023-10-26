package com.nasa.collab.hub.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasa.collab.hub.github.sdk.GithubIssueEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubEventService {

    @Autowired
    private ObjectMapper objectMapper;

    public void handleEvent(String payload) {
        try {
            GithubIssueEvent issueEvent =  objectMapper.readValue(payload, GithubIssueEvent.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
