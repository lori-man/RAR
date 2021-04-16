package com.mro.RAR.auth;

/**
 * 权限凭证
 */
public interface Credentials {
    String getCompanyId();

    String getAccountId();

    String getAccessKey();

    String getSecurityToken();

    void setSecurityToken(String securityToken);

    boolean useSecurityToken();

    void setTokenStatus(boolean status);

    boolean getTokenStatus();
}
