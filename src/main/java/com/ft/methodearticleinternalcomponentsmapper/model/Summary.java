package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Summary {

    private final String bodyXML;
    private final String displayPosition;

    private Summary(@JsonProperty("bodyXML") final String bodyXML, @JsonProperty("displayPosition") final String displayPosition) {
        this.bodyXML = bodyXML;
        this.displayPosition = displayPosition;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getBodyXML() {
        return this.bodyXML;
    }

    public String getDisplayPosition() {
        return this.displayPosition;
    }

    public static Summary.Builder builder() {
        return new Summary.Builder();
    }

    public static class Builder {

        private String bodyXML;
        private String displayPosition;

        public Summary.Builder withBodyXML(String bodyXML) {
            this.bodyXML = bodyXML;
            return this;
        }

        public Summary.Builder withDisplayPosition(String displayPosition) {
            this.displayPosition = displayPosition;
            return this;
        }

        public Summary.Builder withValuesFrom(Summary summary) {
            return this.withBodyXML(summary.bodyXML).withDisplayPosition(summary.displayPosition);
        }

        public Summary build() {
            return new Summary(this.bodyXML, this.displayPosition);
        }
    }
}
