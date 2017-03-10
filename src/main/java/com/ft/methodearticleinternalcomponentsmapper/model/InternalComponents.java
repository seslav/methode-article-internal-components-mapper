package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

public class InternalComponents {

    private Design design;
    private TableOfContents tableOfContents;
    private Topper topper;
    private List<Image> leadImages;

    private String uuid;
    private Date lastModified;
    private String publishReference;

    public InternalComponents(@JsonProperty("design") Design design,
                              @JsonProperty("tableOfContents") TableOfContents tableOfContents,
                              @JsonProperty("topper") Topper topper,
                              @JsonProperty("leadImages") List<Image> leadImages,
                              @JsonProperty("uuid") String uuid,
                              @JsonProperty("lastModified") Date lastModified,
                              @JsonProperty("publishReference") String publishReference) {
        this.design = design;
        this.tableOfContents = tableOfContents;
        this.topper = topper;
        this.leadImages = leadImages;
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
                    uuid,
                    lastModified,
                    publishReference);
        }
    }
}
