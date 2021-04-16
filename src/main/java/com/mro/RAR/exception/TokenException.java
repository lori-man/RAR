package com.mro.RAR.exception;

/**
 * Token 相关异常
 */
public class TokenException extends Exception {
    private String requestUri;
    private String companyId;
    private String userId;
    private String accessKey;

    public TokenException(String message, String requestUri) {
        super(message);
        this.requestUri = requestUri;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }
}
