package com.mro.RAR.exception;

/**
 * rar client 异常
 */
public class ClientException extends RuntimeException {
    private static final long serialVersionUID = 1870835486798448798L;
    private String errorMessage;
    private String requestId;
    private String errorCode;

    public ClientException() {
    }

    public ClientException(String errorMessage) {
        this(errorMessage, (Throwable)null);
    }

    public ClientException(Throwable cause) {
        this((String)null, cause);
    }

    public ClientException(String errorMessage, Throwable cause) {
        super((String)null, cause);
        this.errorMessage = errorMessage;
        this.errorCode = "Unknown";
        this.requestId = "Unknown";
    }

    public ClientException(String errorMessage, String errorCode, String requestId) {
        this(errorMessage, errorCode, requestId, (Throwable)null);
    }

    public ClientException(String errorMessage, String errorCode, String requestId, Throwable cause) {
        this(errorMessage, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
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

    public String getMessage() {
        return this.getErrorMessage() + "\n[ErrorCode]: " + (this.errorCode != null ? this.errorCode : "") + "\n[RequestId]: " + (this.requestId != null ? this.requestId : "");
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

