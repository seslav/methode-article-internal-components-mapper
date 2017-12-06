package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Design {

    private final String theme;
    private final String layout;

    public Design(@JsonProperty("theme") final String theme,
                  @JsonProperty("layout") final String layout) {
        this.theme = theme;
        this.layout = layout;
    }

    public String getTheme() {
        return theme;
    }

    public String getLayout() {
        return layout;
    }
}
