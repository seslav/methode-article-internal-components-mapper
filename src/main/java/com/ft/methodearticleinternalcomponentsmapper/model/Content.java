package com.ft.methodearticleinternalcomponentsmapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Content {
    private String uuid;
    private String type;

    public Content(@JsonProperty("uuid") String uuid,
                   @JsonProperty("type") String type) {
        this.uuid = uuid;
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(uuid, content.uuid) &&
                Objects.equals(type, content.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, type);
    }
}
