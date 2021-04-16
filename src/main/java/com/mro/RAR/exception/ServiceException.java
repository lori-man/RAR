package com.mro.RAR.exception;

/**
 * service client 异常
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 430933593095358673L;
    private String errorMessage;
    private String errorCode;
    private String requestId;
    private String hostId;
    private String rawResponseError;

    public ServiceException() {
    }

    public ServiceException(String errorMessage) {
        super((String)null);
        this.errorMessage = errorMessage;
    }

    public ServiceException(Throwable cause) {
        super((String)null, cause);
    }

    public ServiceException(String errorMessage, Throwable cause) {
        super((String)null, cause);
        this.errorMessage = errorMessage;
    }

    public ServiceException(String errorMessage, String errorCode, String requestId, String hostId) {
        this(errorMessage, errorCode, requestId, hostId, (Throwable)null);
    }

    public ServiceException(String errorMessage, String errorCode, String requestId, String hostId, Throwable cause) {
        this(errorMessage, errorCode, requestId, hostId, (String)null, cause);
    }

    public ServiceException(String errorMessage, String errorCode, String requestId, String hostId, String rawResponseError, Throwable cause) {
        this(errorMessage, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.hostId = hostId;
        this.rawResponseError = rawResponseError;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getHostId() {
        return this.hostId;
    }

    public String getRawResponseError() {
        return this.rawResponseError;
    }

    public void setRawResponseError(String rawResponseError) {
        this.rawResponseError = rawResponseError;
    }

    private String formatRawResponseError() {
        return this.rawResponseError != null && !this.rawResponseError.equals("") ? String.format("\n[ResponseError]:\n%s", this.rawResponseError) : "";
    }

    public String getMessage() {
        return this.getErrorMessage() + "\n[ErrorCode]: " + this.getErrorCode() + "\n[RequestId]: " + this.getRequestId() + "\n[HostId]: " + this.getHostId() + this.formatRawResponseError();
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

