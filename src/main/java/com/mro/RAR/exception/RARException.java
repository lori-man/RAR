package com.mro.RAR.exception;

/**
 * service 异常
 */
public class RARException extends ServiceException {
    private static final long serialVersionUID = -1979779664334663173L;
    private String resourceType;
    private String header;
    private String method;

    public RARException() {
    }

    public RARException(String errorMessage) {
        super(errorMessage);
    }

    public RARException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public RARException(String errorMessage, String errorCode, String requestId, String hostId, String header, String resourceType, String method) {
        this(errorMessage, errorCode, requestId, hostId, header, resourceType, method, (String)null, (Throwable)null);
    }

    public RARException(String errorMessage, String errorCode, String requestId, String hostId, String header, String resourceType, String method, Throwable cause) {
        this(errorMessage, errorCode, requestId, hostId, header, resourceType, method, (String)null, cause);
    }

    public RARException(String errorMessage, String errorCode, String requestId, String hostId, String header, String resourceType, String method, String rawResponseError) {
        this(errorMessage, errorCode, requestId, hostId, header, resourceType, method, rawResponseError, (Throwable)null);
    }

    public RARException(String errorMessage, String errorCode, String requestId, String hostId, String header, String resourceType, String method, String rawResponseError, Throwable cause) {
        super(errorMessage, errorCode, requestId, hostId, rawResponseError, cause);
        this.resourceType = resourceType;
        this.header = header;
        this.method = method;
    }

    public String getResourceType() {
        return this.resourceType;
    }

    public String getHeader() {
        return this.header;
    }

    public String getMethod() {
        return this.method;
    }

    public String getMessage() {
        return super.getMessage() + (this.resourceType == null ? "" : "\n[ResourceType]: " + this.resourceType) + (this.header == null ? "" : "\n[Header]: " + this.header) + (this.method == null ? "" : "\n[Method]: " + this.method);
    }
}
