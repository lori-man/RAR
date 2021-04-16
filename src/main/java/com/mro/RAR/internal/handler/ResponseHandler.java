package com.mro.RAR.internal.handler;

import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.exception.RARException;

/**
 * response处理器
 */
public interface ResponseHandler {
    void handle(ResponseMessage var1) throws RARException, ClientException;
}
