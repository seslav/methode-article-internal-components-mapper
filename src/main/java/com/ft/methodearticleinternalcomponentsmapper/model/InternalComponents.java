package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

public class InternalComponents {

    private final Design design;
    private final TableOfContents tableOfContents;
    private final Topper topper;
    private final List<Image> leadImages;
    private final String unpublishedContentDescription;
    private final String body;

    private final String uuid;
    private final Date lastModified;
    private final String publishReference;

    public InternalComponents(@JsonProperty("design") final Design design,
                              @JsonProperty("tableOfContents") final TableOfContents tableOfContents,
                              @JsonProperty("topper") final Topper topper,
                              @JsonProperty("leadImages") final List<Image> leadImages,
                              @JsonProperty("unpublishedContentDescription") final String unpublishedContentDescription,
                              @JsonProperty("body") final String body,
                              @JsonProperty("uuid") final String uuid,
                              @JsonProperty("lastModified") final Date lastModified,
                              @JsonProperty("publishReference") final String publishReference) {
        this.design = design;
        this.tableOfContents = tableOfContents;
        this.topper = topper;
        this.leadImages = leadImages;
        this.unpublishedContentDescription = unpublishedContentDescription;
        this.body = body;

        this.uuid = uuid;
        this.lastModified = lastModified;
        this.publishReference = publishReference;
    }

    public Design getDesign() {
        return design;
    }

    public TableOfContents getTableOfContents() {
        return tableOfContents;
    }

    public Topper getTopper() {
        return topper;
    }

    public List<Image> getLeadImages() {
        return leadImages;
    }

    public String getUnpublishedContentDescription() {
        return unpublishedContentDescription;
    }

    public String getBody() {
        return body;
    }

    public String getUuid() {
        return uuid;
    }

    @NotNull
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'",
            timezone = "UTC"
    )
    public Date getLastModified() {
        return lastModified;
    }

    public String getPublishReference() {
        return publishReference;
    }

    public static InternalComponents.Builder builder() {
        return new InternalComponents.Builder();
    }

    public static class Builder {
        private Design design;
        private TableOfContents tableOfContents;
        private Topper topper;
        private List<Image> leadImages;
        private String unpublishedContentDescription;
        private String body;

        private String uuid;
        private String publishReference;
        private Date lastModified;

        public Builder() {
        }

        public InternalComponents.Builder withDesign(Design design) {
            this.design = design;
            return this;
        }

        public InternalComponents.Builder withTableOfContents(TableOfContents tableOfContents) {
            this.tableOfContents = tableOfContents;
            return this;
        }

        public InternalComponents.Builder withTopper(Topper topper) {
            this.topper = topper;
            return this;
        }

        public InternalComponents.Builder withLeadImages(List<Image> leadImages) {
            this.leadImages = leadImages;
            return this;
        }

        public InternalComponents.Builder withUnpublishedContentDescription(String unpublishedContentDescription) {
            this.unpublishedContentDescription = unpublishedContentDescription;
            return this;
        }

        public InternalComponents.Builder withXMLBody(String body) {
            this.body = body;
            return this;
        }

        public InternalComponents.Builder withUuid(UUID uuid) {
            this.uuid = uuid.toString();
            return this;
        }

        public InternalComponents.Builder withPublishReference(String publishReference) {
            this.publishReference = publishReference;
            return this;
        }

        public InternalComponents.Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public InternalComponents build() {
            return new InternalComponents(
                    design,
                    tableOfContents,
                    topper,
                    leadImages,
                    unpublishedContentDescription,
                    body,
                    uuid,
                    lastModified,
                    publishReference);
        }
    }
}
