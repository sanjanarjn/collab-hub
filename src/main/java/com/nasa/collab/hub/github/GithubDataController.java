package com.nasa.collab.hub.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/home")
public class GithubDataController {

    @Autowired
    private GithubDataService githubDataService;

    @GetMapping("/{user-id}/projects")
    public ResponseEntity<?> getRepositoriesBasedOnPreference(@PathVariable("user-id") long userId) throws Exception {
        return new ResponseEntity<>(githubDataService.fetchRepositoriesBasedOnPreference(userId), HttpStatus.OK);
    }

    @GetMapping("/{user-id}/issues")
    public ResponseEntity<?> getIssuesBasedOnPreference(@PathVariable("user-id") long userId, @RequestParam(required = false, name="label") Optional<String> issueLabel) throws Exception {
        return new ResponseEntity<>(githubDataService.fetchIssuesBasedOnPreference(userId, issueLabel), HttpStatus.OK);
    }

}
