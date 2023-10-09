package com.nasa.collab.hub.github.sdk;

import lombok.Getter;

public enum IssueLabelType {

    GOOD_FIRST_ISSUE("\"good first issue\""), BUG("bug"), DOCUMENTATION("documentation");

    @Getter
    private String label;
    IssueLabelType(String label) {
        this.label = label;
    }
}
