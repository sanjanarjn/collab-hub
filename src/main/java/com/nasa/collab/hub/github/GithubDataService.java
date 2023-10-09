package com.nasa.collab.hub.github;

import com.nasa.collab.hub.github.sdk.GithubIssue;
import com.nasa.collab.hub.github.sdk.GithubRepository;
import com.nasa.collab.hub.github.sdk.IssueLabelType;
import com.nasa.collab.hub.preference.Preference;
import com.nasa.collab.hub.preference.PreferenceService;
import com.nasa.collab.hub.preference.PreferenceType;
import com.nasa.collab.hub.user.User;
import com.nasa.collab.hub.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GithubDataService {

    @Autowired
    private GithubIntegrationService githubIntegrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PreferenceService preferenceService;

    public List<GithubRepository> fetchRepositoriesBasedOnPreference(long userId) throws Exception {

        Map<String, String> queryParams = getQueryParamsBasedOnUserPreference(userId, Arrays.stream(PreferenceType.values()).collect(Collectors.toSet()));
        return githubIntegrationService.searchForRepositories(queryParams);
    }

    public List<GithubIssue> fetchIssuesBasedOnPreference(long userId, Optional<String> issueLabelOptional) throws Exception {

        Map<String, String> queryParams = getQueryParamsBasedOnUserPreference(userId, new HashSet<>(Arrays.asList(PreferenceType.TECHNOLOGY)));

        String searchQuery = queryParams.get("q");
        StringBuilder issueSearchQuery = new StringBuilder(searchQuery);

        String issueLabel = issueLabelOptional.isPresent() ?
                Arrays.stream(issueLabelOptional.get().split(",")).map(label -> "\"" + label + "\"").collect(Collectors.joining(",")) :
                Arrays.stream(IssueLabelType.values()).map(label -> label.getLabel()).collect(Collectors.joining(","));
        issueSearchQuery.append(" ").append("label:" + issueLabel);
        issueSearchQuery.append(" ").append("is:issue");
        issueSearchQuery.append(" ").append("state:open");

        queryParams.put("q", issueSearchQuery.toString());
        return githubIntegrationService.searchForIssues(queryParams);
    }

    private Map<String, String> getQueryParamsBasedOnUserPreference(long userId, Set<PreferenceType> preferenceTypesToConsider) throws Exception {
        User user = userService.getUserById(userId);
        Map<PreferenceType, List<Preference>> userPreference = user.getUserPreferences();
        Map<String, String> queryParams = new HashMap<>();

        List<String> individualQueries = new ArrayList<>();
        for (PreferenceType preferenceType : userPreference.keySet()) {
            if (!preferenceTypesToConsider.contains(preferenceType))
                continue;

            List<Preference> preferenceIds = userPreference.get(preferenceType);
            List<Preference> matchingPreferences = preferenceService.getPreferencesByIds(preferenceType, preferenceIds.stream().map(Preference::getId).collect(Collectors.toList()));

            switch (preferenceType) {
                case DOMAIN, FOCUS_AREA: {
                    individualQueries.add(getDefaultSearchQuery(preferenceType, matchingPreferences));
                    break;
                }
                case TECHNOLOGY: {
                    individualQueries.add(preferenceType.getKey().get() + ":" + matchingPreferences.stream().map(Preference::getName).collect(Collectors.joining(",")));
                    break;
                }
            }
        }
        queryParams.put("q", String.join(" ", individualQueries));
        return queryParams;
    }

    private static String getDefaultSearchQuery(PreferenceType preferenceType, List<Preference> matchingPreferences) {

        List<String> searchAndFieldQueries = new ArrayList<>();
        List<String> searchQueries = matchingPreferences.stream().map(Preference::getName).collect(Collectors.toList());
        for (String searchQuery : searchQueries) {
            String fieldQuery = preferenceType.getFields().stream().map(field -> "in:" + field).collect(Collectors.joining(" "));
            searchAndFieldQueries.add("(" + searchQuery + " " + fieldQuery + ")");
        }

        return String.join(" OR ", searchAndFieldQueries);
    }
}
