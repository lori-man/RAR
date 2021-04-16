package com.mro.RAR.common.utils;

import com.mro.RAR.event.ExceptionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志工具类
 */
public class LogUtils {
    private static final Log log = LogFactory.getLog("com.mro.RAR");
    private static List<String> errorCodeFilterList = new ArrayList();

    private static ExceptionHandler exceptionHandler;

    public LogUtils() {
    }

    public static void logWarn(String message) {
        log.warn(message);
    }
    public static void logInfo(String message) {
        log.info(message);
    }

    public static <ExType> void logException(String messagePrefix, ExType ex) {
        logException(messagePrefix, ex, false);
    }

    public static <ExType> void logException(String messagePrefix, ExType ex, boolean logEnabled) {
        assert ex instanceof Exception;

        String detailMessage = messagePrefix + ((Exception)ex).getMessage();

        if (logEnabled && exceptionHandler != null) {
            exceptionHandler.handle((Exception) ex);
        }

        log.error(ex);
    }

    public static void setExceptionHandler(ExceptionHandler tokenExceptionHandler) {
        LogUtils.exceptionHandler = tokenExceptionHandler;
    }

    static {
        errorCodeFilterList.add("NoSuchBucket");
        errorCodeFilterList.add("NoSuchKey");
        errorCodeFilterList.add("NoSuchUpload");
        errorCodeFilterList.add("NoSuchCORSConfiguration");
        errorCodeFilterList.add("NoSuchWebsiteConfiguration");
        errorCodeFilterList.add("NoSuchLifecycle");
    }
}
