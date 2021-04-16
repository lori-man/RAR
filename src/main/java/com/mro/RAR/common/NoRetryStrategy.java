package com.mro.RAR.common;

/**
 * 无重试策略
 */
public class NoRetryStrategy extends RetryStrategy {
    public NoRetryStrategy() {
    }

    public boolean shouldRetry(Exception ex, RequestMessage request, ResponseMessage response, int retries) {
        return false;
    }
}
