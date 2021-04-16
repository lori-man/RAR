package com.mro.RAR.event;

/**
 * 异常 handler ,处理类型异常:ResponseParseException
 */
public interface ExceptionHandler {
    void handle(Exception e);
}