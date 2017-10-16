package com.ft.methodearticleinternalcomponentsmapper.model.concordance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class ConceptView {

    public ConceptView(@JsonProperty("id") String id, @JsonProperty("apiUrl") String apiUrl) {
        this.id = id;
        this.apiUrl = apiUrl;
    }

    private String id;
    private String apiUrl;

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getApiUrl() {
        return apiUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConceptView that = (ConceptView) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(apiUrl, that.apiUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, apiUrl);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("apiUrl", apiUrl)
                .toString();
    }

}
