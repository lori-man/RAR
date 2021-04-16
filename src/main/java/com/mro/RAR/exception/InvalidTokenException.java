package com.mro.RAR.exception;

public class InvalidTokenException extends GatewayLimitException {
    public InvalidTokenException() {

    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
