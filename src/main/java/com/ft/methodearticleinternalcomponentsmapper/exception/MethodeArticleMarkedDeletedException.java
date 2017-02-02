package com.ft.methodearticleinternalcomponentsmapper.exception;

import java.util.UUID;

public class MethodeArticleMarkedDeletedException extends RuntimeException {

    public MethodeArticleMarkedDeletedException(UUID uuid) {
        super(String.format("Story has been marked as deleted %s", uuid));
    }
}
