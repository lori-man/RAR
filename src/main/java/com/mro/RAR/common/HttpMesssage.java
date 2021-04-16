package com.mro.RAR.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Http 请求体
 */
public abstract class HttpMesssage {
    private Map<String, String> headers;
    private InputStream content;
    private long contentLength;

    public HttpMesssage() {
        this.headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public InputStream getContent() {
        return this.content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public void close() throws IOException {
        if (this.content != null) {
            this.content.close();
            this.content = null;
        }

    }
}
