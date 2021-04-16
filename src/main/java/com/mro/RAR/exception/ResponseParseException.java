package com.mro.RAR.exception;

import java.util.Map;

public class ResponseParseException extends Exception {
    private static final long serialVersionUID = -6660159156997037589L;

    private Map parameters;
    private String uri;

    public ResponseParseException() {
    }

    public ResponseParseException(String message) {
        super(message);
    }

    public ResponseParseException(Throwable cause) {
        super(cause);
    }

    public ResponseParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
