package com.nasa.collab.hub.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubEventController {

    @Autowired
    private GithubEventService eventService;

    @PostMapping("/events")
    public void handleGithubWebhookEvent(@RequestBody String payload) {
        eventService.handleEvent(payload);
    }
}
