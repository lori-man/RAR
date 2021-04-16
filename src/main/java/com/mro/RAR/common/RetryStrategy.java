package com.mro.RAR.common;

/**
 * 重试策略
 */
public abstract class RetryStrategy {
    private static final int DEFAULT_RETRY_PAUSE_SCALE = 300;

    public RetryStrategy() {
    }

    public abstract boolean shouldRetry(Exception var1, RequestMessage var2, ResponseMessage var3, int var4);

    public long getPauseDelay(int retries) {
        int scale = 300;
        long delay = (long)Math.pow(2.0D, (double)retries) * (long)scale;
        return delay;
    }
}

