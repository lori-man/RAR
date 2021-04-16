package com.mro.RAR.auth;

public class CredentialsException extends RuntimeException {
    public CredentialsException(String message) {
        super(message);
    }

    public CredentialsException(Throwable cause) {
        super(cause);
    }

    public CredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
