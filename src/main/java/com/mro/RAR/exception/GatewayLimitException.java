package com.mro.RAR.exception;

/**
 * 网关限制异常
 */
public class GatewayLimitException extends ResponseParseException {
    public GatewayLimitException() {

    }

    public GatewayLimitException(String message) {
        super(message);
    }
}
