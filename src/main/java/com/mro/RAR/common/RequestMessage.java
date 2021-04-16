package com.mro.RAR.common;

import com.mro.RAR.model.WebServiceRequest;
import com.mro.RAR.model.options.HttpMethod;

import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestMessage extends HttpMesssage {
    private URI endpoint;
    private Map<String, String> parameters;
    private URL absoluteUrl;
    private HttpMethod httpMethod;
    private final WebServiceRequest originalRequest;

    private boolean useChunkEncoding;
    //是否使用认证标识(token)
    private boolean useUrlSignature;

    public RequestMessage(WebServiceRequest originalRequest) {
        this.httpMethod = HttpMethod.GET;
        this.parameters = new LinkedHashMap();
        this.useUrlSignature = false;
        this.useChunkEncoding = false;
        this.originalRequest = originalRequest == null ? WebServiceRequest.NOOP : originalRequest;
    }

    public URI getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
    public void setParameters(Map<String, String> parameters) {
        this.parameters.clear();
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters.putAll(parameters);
        }

    }

    public URL getAbsoluteUrl() {
        return absoluteUrl;
    }

    public void setAbsoluteUrl(URL absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public boolean isUseChunkEncoding() {
        return this.useChunkEncoding;
    }

    public void setUseChunkEncoding(boolean useChunkEncoding) {
        this.useChunkEncoding = useChunkEncoding;
    }

    public boolean isUseUrlSignature() {
        return useUrlSignature;
    }

    public void setUseUrlSignature(boolean useUrlSignature) {
        this.useUrlSignature = useUrlSignature;
    }

    public WebServiceRequest getOriginalRequest() {
        return originalRequest;
    }

    public String toString() {
        return "Endpoint: " + this.getEndpoint().getHost() + ", Headers:" + this.getHeaders();
    }

}
