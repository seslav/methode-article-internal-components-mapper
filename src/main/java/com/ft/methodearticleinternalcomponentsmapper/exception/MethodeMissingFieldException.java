package com.ft.methodearticleinternalcomponentsmapper.exception;

public class MethodeMissingFieldException extends InvalidMethodeContentException {
	private static final long serialVersionUID = 1957685706838057455L;
	private final String fieldName;

    public MethodeMissingFieldException(String uuid, String fieldName, String wrapper) {
        super(uuid, String.format("%s %s missing field: %s", wrapper, uuid, fieldName));
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
