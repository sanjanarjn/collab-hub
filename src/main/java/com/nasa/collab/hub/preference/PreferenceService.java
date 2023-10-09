package com.nasa.collab.hub.preference;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PreferenceService {

    private List<Preference> domains;
    private List<Preference> focusAreas;
    private List<Preference> technologies;

    Map<PreferenceType, List<Preference>> preferences;

    @PostConstruct
    public void loadStaticPredefinedData() {
        domains = new ArrayList<>();
        domains.add(new Preference(1, "science", "Science"));
        domains.add(new Preference(2, "health", "Health & Fitness"));
        domains.add(new Preference(3, "gaming", "Gaming"));
        domains.add(new Preference(4, "finance", "Finance"));
        domains.add(new Preference(5, "commerce", "Commerce"));
        domains.add(new Preference(6, "environment", "Environment"));

        focusAreas = new ArrayList<>();
        focusAreas.add(new Preference(1, "web", "Web Applications"));
        focusAreas.add(new Preference(2, "backend", "Backend Development"));
        focusAreas.add(new Preference(3, "mobile", "Mobile Applications"));

        technologies = new ArrayList<>();
        technologies.add(new Preference(1, "nodejs", "NodeJs"));
        technologies.add(new Preference(2, "java", "Java"));
        technologies.add(new Preference(3, "go", "GoLang"));
        technologies.add(new Preference(4, "javascript", "Javascript"));
        technologies.add(new Preference(5, "python", "Python"));
        technologies.add(new Preference(6, "swift", "Swift"));

        preferences = new HashMap<>();
        preferences.put(PreferenceType.DOMAIN, domains);
        preferences.put(PreferenceType.FOCUS_AREA, focusAreas);
        preferences.put(PreferenceType.TECHNOLOGY, technologies);
    }

    public List<Preference> getPreferencesByIds(PreferenceType preferenceType, List<Long> preferenceIds) {
        List<Preference> matchingPreferences = new ArrayList<>();
        List<Preference> searchSpace = preferences.get(preferenceType);
        for(long preferenceId : preferenceIds) {
           for(Preference preference: searchSpace) {
               if(preference.getId() == preferenceId) {
                   matchingPreferences.add(preference);
               }
           }
        }
        return matchingPreferences;
    }
}
