package com.mro.RAR.auth;

/**
 * 权限凭证供应器
 */
public interface CredentialsProvider {
    CredentialsHandler getCredentialsHandler();

    void setCredentialsHandler(CredentialsHandler credentialsHandler);
}
