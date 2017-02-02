package com.ft.methodearticleinternalcomponentsmapper.exception;

public class MethodeArticleMapperUnavailableException extends RuntimeException {

    public MethodeArticleMapperUnavailableException(int statusCode) {
        super(String.format("Validation failed. Received status code: %s", statusCode));
    }
}
