package com.ft.methodearticleinternalcomponentsmapper.exception;

import java.util.UUID;

public class MethodeArticleHasNoInternalComponentsException extends RuntimeException {
    public MethodeArticleHasNoInternalComponentsException(UUID uuid) {
        super(String.format("Story %s does not have a topper.", uuid));
    }
}
