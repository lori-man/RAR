package com.mro.RAR.auth;

/**
 * 默认认证凭证
 */
public class DefaultCredentials implements Credentials {
    private final String companyId;
    private final String accountId;
    private final String accessKey;
    private String securityToken;
    private boolean useToken;
    private boolean tokenStatus;

    public DefaultCredentials(String companyId, String accountId, String accessKey) {
        this(companyId, accountId, accessKey, (String) null);
    }

    public DefaultCredentials(String companyId, String accountId, String accessKey, String securityToken) {
        this.useToken = true;
        this.tokenStatus = false;

        if (companyId != null && !companyId.equals("")
                && accountId != null && !accountId.equals("")
                && accessKey != null && !accessKey.equals("")) {
            this.companyId = companyId;
            this.accountId = accountId;
            this.accessKey = accessKey;
            this.securityToken = securityToken;
        } else {
            throw new InvalidCredentialsException(companyId, accountId, accessKey, "company or Access key id should not be null or empty.");
        }

        if (securityToken != null) {
            tokenStatus = true;
        }
    }

    public String getAccountId() {
        return accountId;
    }


    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        if (securityToken != null) {
            this.securityToken = securityToken;
            this.useToken = true;
            this.tokenStatus = true;
        }
    }

    public boolean useSecurityToken() {
        return this.useToken;
    }

    public void setTokenStatus(boolean tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public boolean getTokenStatus() {
        return this.tokenStatus;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public boolean isUseToken() {
        return useToken;
    }

    public void setUseToken(boolean useToken) {
        this.useToken = useToken;
    }

    public boolean isTokenStatus() {
        return tokenStatus;
    }

    public String getCompanyId() {
        return companyId;
    }
}
