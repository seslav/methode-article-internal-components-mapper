package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableOfContents {

    private final String sequence;
    private final String labelType;

    public TableOfContents(@JsonProperty("sequence") final String sequence,
                           @JsonProperty("labelType") final String labelType) {
        this.sequence = sequence;
        this.labelType = labelType;
    }

    public String getSequence() {
        return sequence;
    }

    public String getLabelType() {
        return labelType;
    }
}