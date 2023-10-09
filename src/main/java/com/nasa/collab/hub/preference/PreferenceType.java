package com.nasa.collab.hub.preference;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum PreferenceType {
    DOMAIN(Optional.empty(), Arrays.asList("topics")),
    FOCUS_AREA(Optional.empty(), Arrays.asList("readme", "topics", "description", "name", "language")),
    TECHNOLOGY(Optional.of("language"), Collections.EMPTY_LIST);

    @Getter
    private List<String> fields;

    @Getter
    private Optional<String> key;
    private PreferenceType(Optional<String> key, List<String> fields) {
        this.key = key; this.fields = fields;
    }
}
