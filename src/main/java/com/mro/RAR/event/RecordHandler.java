package com.mro.RAR.event;


import com.mro.RAR.common.RequestMessage;

/**
 * 记录失败的请求处理
 */
public interface RecordHandler {
    void handle(RequestMessage requestMessage);
}
