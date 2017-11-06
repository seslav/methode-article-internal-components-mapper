package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;

public class AlternativeStandfirsts {

    private final String promotionalStandfirstVariant;

    public static Builder builder() {
        return new Builder();
    }

    private AlternativeStandfirsts(@JsonProperty("promotionalStandfirstVariant") String promotionalStandfirstVariant) {
        this.promotionalStandfirstVariant = promotionalStandfirstVariant;
    }

    public String getPromotionalStandfirstVariant() {
        return promotionalStandfirstVariant;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("promotionalStandfirstVariant", promotionalStandfirstVariant).toString();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || (o.getClass() != AlternativeStandfirsts.class)) {
            return false;
        }

        final AlternativeStandfirsts that = (AlternativeStandfirsts) o;

        return Objects.equals(this.promotionalStandfirstVariant, that.promotionalStandfirstVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promotionalStandfirstVariant);
    }

    public static class Builder {

        private String promotionalStandfirstVariant;

        public Builder withPromotionalStandfirstVariant(String promotionalStandfirstVariant) {
            this.promotionalStandfirstVariant = promotionalStandfirstVariant;
            return this;
        }

        public Builder withValuesFrom(AlternativeStandfirsts alternativeStandfirsts) {
            return withPromotionalStandfirstVariant(alternativeStandfirsts.getPromotionalStandfirstVariant());
        }

        public AlternativeStandfirsts build() {
            return new AlternativeStandfirsts(promotionalStandfirstVariant);
        }
    }
}
