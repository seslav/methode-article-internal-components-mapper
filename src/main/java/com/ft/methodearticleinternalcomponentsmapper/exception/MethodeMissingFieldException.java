package com.ft.methodearticleinternalcomponentsmapper.exception;

public class MethodeMissingFieldException extends InvalidMethodeContentException {
	private static final long serialVersionUID = 1957685706838057455L;
	private final String fieldName;

    public MethodeMissingFieldException(String uuid, String fieldName) {
        super(uuid, String.format("content is missing field. uuid=%s, field=%s", uuid, fieldName));
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
