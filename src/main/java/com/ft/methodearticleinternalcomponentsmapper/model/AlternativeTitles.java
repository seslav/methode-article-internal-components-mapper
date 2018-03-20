package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;

public class AlternativeTitles {

    private final String shortTeaser;
    private final String promotionalTitleVariant;
    private final String htmlTitle;

    public static Builder builder() {
        return new Builder();
    }

    private AlternativeTitles(@JsonProperty("shortTeaser") String shortTeaser,
                              @JsonProperty("promotionalTitleVariant") String promotionalTitleVariant,
                              @JsonProperty("htmlTitle") String htmlTitle) {
        this.shortTeaser = shortTeaser;
        this.promotionalTitleVariant = promotionalTitleVariant;
        this.htmlTitle = htmlTitle;
    }

    public String getShortTeaser() {
        return shortTeaser;
    }

    public String getPromotionalTitleVariant() {
        return promotionalTitleVariant;
    }

    public String getHtmlTitle() {
        return htmlTitle;
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
        private String htmlTitle;

        public Builder withShortTeaser(String title) {
            this.shortTeaser = title;
            return this;
        }

        public Builder withPromotionalTitleVariant(String promotionalTitleVariant) {
            this.promotionalTitleVariant = promotionalTitleVariant;
            return this;
        }

        public Builder withHtmlTitle(String htmlTitle) {
            this.htmlTitle = htmlTitle;
            return this;
        }

        public Builder withValuesFrom(AlternativeTitles titles) {
            return withShortTeaser(titles.getShortTeaser())
                    .withPromotionalTitleVariant(titles.getPromotionalTitleVariant())
                    .withHtmlTitle(titles.getHtmlTitle());
        }

        public AlternativeTitles build() {
            return new AlternativeTitles(shortTeaser, promotionalTitleVariant, htmlTitle);
        }
    }
}
