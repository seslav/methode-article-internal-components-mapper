package com.ft.methodearticleinternalcomponentsmapper.model.concordance;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Identifier {

    private String authority;
    private String identifierValue;

    public Identifier(@JsonProperty("authority") String authority, @JsonProperty("identifierValue") String identifierValue) {
        this.authority = requireNonNull(authority);
        this.identifierValue = requireNonNull(identifierValue);
    }

    public String getAuthority() {
        return authority;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(authority, that.authority) &&
                Objects.equals(identifierValue, that.identifierValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority, identifierValue);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("authority", authority)
                .append("identifierValue", identifierValue)
                .toString();
    }
}
