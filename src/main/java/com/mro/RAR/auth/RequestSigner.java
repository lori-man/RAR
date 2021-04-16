package com.mro.RAR.auth;


import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.exception.ClientException;

/**
 * 请求认证签名
 */
public interface RequestSigner {

    /**
     * 为请求添加标识,版本提供拓展性
     * @param request
     * @throws ClientException
     */
    void sign(RequestMessage request) throws ClientException;
}
