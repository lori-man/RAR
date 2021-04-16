package com.mro.RAR.internal.handler;

import com.mro.RAR.common.RequestMessage;

/**
 * request 处理器
 */
public interface RequestHandler {
    void handle(RequestMessage message);
}
