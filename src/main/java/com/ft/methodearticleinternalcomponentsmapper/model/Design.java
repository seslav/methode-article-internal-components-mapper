package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Design {

    private final String theme;

    public Design(@JsonProperty("theme") final String theme) {
        this.theme = theme;
    }

    public String getTheme() {
        return theme;
    }
}
