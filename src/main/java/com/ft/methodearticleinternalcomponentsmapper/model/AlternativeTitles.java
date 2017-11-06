package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;

public class AlternativeTitles {

    private final String shortTeaser;
    private final String promotionalTitleVariant;

    public static Builder builder() {
        return new Builder();
    }

    private AlternativeTitles(@JsonProperty("shortTeaser") String shortTeaser, @JsonProperty("promotionalTitleVariant") String promotionalTitleVariant) {
        this.shortTeaser = shortTeaser;
        this.promotionalTitleVariant = promotionalTitleVariant;
    }

    public String getShortTeaser() {
        return shortTeaser;
    }

    public String getPromotionalTitleVariant() {
        return promotionalTitleVariant;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("shortTeaser", shortTeaser).add("promotionalTitleVariant", promotionalTitleVariant).toString();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || (o.getClass() != AlternativeTitles.class)) {
            return false;
        }

        final AlternativeTitles that = (AlternativeTitles) o;

        return Objects.equals(this.shortTeaser, that.shortTeaser) && Objects.equals(this.promotionalTitleVariant, that.promotionalTitleVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortTeaser, promotionalTitleVariant);
    }

    public static class Builder {

        private String shortTeaser;
        private String promotionalTitleVariant;

        public Builder withShortTeaser(String title) {
            this.shortTeaser = title;
            return this;
        }

        public Builder withPromotionalTitleVariant(String promotionalTitleVariant) {
            this.promotionalTitleVariant = promotionalTitleVariant;
            return this;
        }

        public Builder withValuesFrom(AlternativeTitles titles) {
            return withShortTeaser(titles.getShortTeaser()).withPromotionalTitleVariant(titles.getPromotionalTitleVariant());
        }

        public AlternativeTitles build() {
            return new AlternativeTitles(shortTeaser, promotionalTitleVariant);
        }
    }
}
