package com.mro.RAR.auth;

/**
 * invaild credential exception:出现此异常需停止后续动作
 */
public class InvalidCredentialsException extends CredentialsException {
    private static final long serialVersionUID = 1L;

    private String companyId;
    private String userId;
    private String accessKey;

    public InvalidCredentialsException(String companyId, String userId,
                                       String accessKey, String messsage) {
        super(messsage);
        this.companyId = companyId;
        this.userId = userId;
        this.accessKey = accessKey;
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
