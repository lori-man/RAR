package com.mro.RAR.model;


import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 内部request封装基类
 */
public abstract class WebServiceRequest {
    public static final WebServiceRequest NOOP = new WebServiceRequest() {
    };

    /**
     * 是否使用log 默认为true
     */
    private boolean logEnabled;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private Set<String> additionalHeaderNames;

    public WebServiceRequest() {
        this.logEnabled = true;
        this.parameters = new LinkedHashMap();
        this.headers = new LinkedHashMap();
        this.additionalHeaderNames = new HashSet();
    }


    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
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

    public Set<String> getAdditionalHeaderNames() {
        return this.additionalHeaderNames;
    }

    public void setAdditionalHeaderNames(Set<String> additionalHeaderNames) {
        this.additionalHeaderNames = additionalHeaderNames;
    }

    public void addAdditionalHeaderName(String name) {
        this.additionalHeaderNames.add(name);
    }

    public boolean isLogEnabled() {
        return this.logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }
}
