package com.mro.RAR.auth;

/**
 * 默认认证凭证供应器
 */
public class DefaultCredentialProvider implements CredentialsProvider {
    private volatile CredentialsHandler credentialsHandler;

    public DefaultCredentialProvider(CredentialsHandler credentialsHandler) {
        if (credentialsHandler == null) {
            throw new InvalidCredentialsException("credentialsHandler should not be null or empty.");
        }
        this.setCredentialsHandler(credentialsHandler);
    }


    public synchronized void setCredentialsHandler(CredentialsHandler credentialsHandler) {
        if (credentialsHandler == null) {
            throw new InvalidCredentialsException("credsHandler should not be null.");
        } else {
            this.credentialsHandler = credentialsHandler;
        }
    }

    public CredentialsHandler getCredentialsHandler() {
        if (this.credentialsHandler == null) {
            throw new InvalidCredentialsException("Invalid credentialsHanler");
        } else {
            return this.credentialsHandler;
        }
    }
}
