package com.mro.RAR.internal.parser;

import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.exception.ResponseParseException;

/**
 * response 解析器基类
 */
public interface ResponseParser<T> {
    T parse(ResponseMessage var1) throws ResponseParseException;
}
