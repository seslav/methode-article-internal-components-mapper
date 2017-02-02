package com.ft.methodearticleinternalcomponentsmapper.exception;

import java.util.UUID;

public class MethodeArticleNotEligibleForPublishException extends RuntimeException {

    public MethodeArticleNotEligibleForPublishException(UUID uuid) {
        super(String.format("Story not eligible for publish %s", uuid));
    }
}
