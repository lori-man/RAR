package com.mro.RAR.common.utils;

import com.mro.RAR.RARUtils;
import com.mro.RAR.exception.ClientException;
import com.mro.RAR.exception.RARException;
import com.mro.RAR.model.RARErrorResult;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 异常管理工厂
 */
public class ExceptionFactory {
    public ExceptionFactory() {
    }

    public static ClientException createNetworkException(IOException ex) {
        String requestId = "Unknown";
        String errorCode = "Unknown";
        if (ex instanceof SocketTimeoutException) {
            errorCode = "SocketTimeout";
        } else if (ex instanceof SocketException) {
            errorCode = "SocketException";
        } else if (ex instanceof ConnectTimeoutException) {
            errorCode = "ConnectionTimeout";
        } else if (ex instanceof UnknownHostException) {
            errorCode = "UnknownHost";
        } else if (ex instanceof HttpHostConnectException) {
            errorCode = "ConnectionRefused";
        } else if (ex instanceof NoHttpResponseException) {
            errorCode = "ConnectionTimeout";
        } else if (ex instanceof ClientProtocolException) {
            Throwable cause = ex.getCause();
            if (cause instanceof NonRepeatableRequestException) {
                errorCode = "NonRepeatableRequest";
                return new ClientException(cause.getMessage(), errorCode, requestId, cause);
            }
        }

        return new ClientException(ex.getMessage(), errorCode, requestId, ex);
    }

    public static RARException createInvalidResponseException(String requestId, Throwable cause) {
        return createInvalidResponseException(requestId, RARUtils.COMMON_RESOURCE_MANAGER.getFormattedString("FailedToParseResponse", new Object[]{cause.getMessage()}));
    }

    public static RARException createInvalidResponseException(String requestId, String rawResponseError, Throwable cause) {
        return createInvalidResponseException(requestId, RARUtils.COMMON_RESOURCE_MANAGER.getFormattedString("FailedToParseResponse", new Object[]{cause.getMessage()}), rawResponseError);
    }

    public static RARException createInvalidResponseException(String requestId, String message) {
        return createRARException(requestId, "InvalidResponse", message);
    }

    public static RARException createInvalidResponseException(String requestId, String message, String rawResponseError) {
        return createRARException(requestId, "InvalidResponse", message, rawResponseError);
    }

    public static RARException createRARException(String requestId, String errorCode, String message) {
        return new RARException(message, errorCode, requestId, (String)null, (String)null, (String)null, (String)null);
    }

    public static RARException createRARException(RARErrorResult errorResult, String rawResponseError) {
        return new RARException(errorResult.Message, errorResult.Code, errorResult.RequestId, errorResult.HostId,
                errorResult.Header, errorResult.ResourceType, errorResult.Method, rawResponseError);
    }

    public static RARException createRARException(String requestId, String errorCode, String message, String rawResponseError) {
        return new RARException(message, errorCode, requestId, (String)null, (String)null, (String)null, (String)null, rawResponseError);
    }

    public static RARException createUnknownRARException(String requestId, int statusCode) {
        String message = "No body in response, http status code " + Integer.toString(statusCode);
        return new RARException(message, "Unknown", requestId, (String)null, (String)null, (String)null, (String)null);
    }
}
