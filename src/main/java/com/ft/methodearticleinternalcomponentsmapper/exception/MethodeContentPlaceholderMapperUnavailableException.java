package com.ft.methodearticleinternalcomponentsmapper.exception;

public class MethodeContentPlaceholderMapperUnavailableException extends RuntimeException {

    public MethodeContentPlaceholderMapperUnavailableException(int statusCode) {
        super(String.format("Validation failed. Received status code: %s", statusCode));
    }
}
