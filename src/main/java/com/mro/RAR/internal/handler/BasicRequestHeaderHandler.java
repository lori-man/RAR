package com.mro.RAR.internal.handler;

import com.mro.RAR.ClientConfiguration;
import com.mro.RAR.common.RequestMessage;

/**
 * request basic header
 */
public class BasicRequestHeaderHandler implements RequestHandler {
    private ClientConfiguration configuration;

    public BasicRequestHeaderHandler(ClientConfiguration clientConfiguration) {
        this.configuration = clientConfiguration;
    }

    @Override
    public void handle(RequestMessage message) {
        message.addHeader("User-Agent", configuration.getUserAgent());
        message.addHeader("User-Agent-os", "saas");
    }
}
