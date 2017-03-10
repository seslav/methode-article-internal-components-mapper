package com.ft.methodearticleinternalcomponentsmapper.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Topper {

    private String headline;
    private String standfirst;
    private String backgroundColour;
    private String layout;

    @Deprecated
    private String theme;

    @Deprecated
    private List<Image> images;

    public Topper(@JsonProperty("headline") String headline,
                  @JsonProperty("standfirst") String standfirst,
                  @JsonProperty("backgroundColour") String backgroundColour,
                  @JsonProperty("layout") String layout,
                  @JsonProperty("theme") String theme,
                  @JsonProperty("image") List<Image> images) {
        this.headline = headline;
        this.standfirst = standfirst;
        this.backgroundColour = backgroundColour;
        this.layout = layout;
        this.theme = theme;
        this.images = images;
    }

    public String getHeadline() {
        return headline;
    }

    public String getStandfirst() {
        return standfirst;
    }

    public String getBackgroundColour() {
        return backgroundColour;
    }

    public String getLayout() {
        return layout;
    }

    public String getTheme() {
        return theme;
    }

    public List<Image> getImages() {
        return images;
    }
}
