package com.mro.RAR.internal;

import com.mro.RAR.ClientConfiguration;
import com.mro.RAR.ServiceClient;
import com.mro.RAR.common.RequestMessage;
import com.mro.RAR.common.utils.CodingUtils;
import com.mro.RAR.common.utils.DateUtil;
import com.mro.RAR.model.WebServiceRequest;
import com.mro.RAR.model.options.HttpMethod;

import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class RARRequestMessageBuilder {
    private URI endpoint;
    private HttpMethod method;
    private Map<String, String> headers;
    private Map<String, String> parameters;
    private InputStream inputStream;
    private long inputSize;
    private ServiceClient innerClient;
    private boolean useChunkEncoding;
    private WebServiceRequest originalRequest;

    public RARRequestMessageBuilder(ServiceClient innerClient) {
        this.method = HttpMethod.GET;
        this.headers = new HashMap();
        this.parameters = new LinkedHashMap();
        this.inputSize = 0L;
        this.useChunkEncoding = false;
        this.innerClient = innerClient;
    }

    public URI getEndpoint() {
        return this.endpoint;
    }

    public RARRequestMessageBuilder setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
        return this;
    }
    public RARRequestMessageBuilder setEndpoint(String endpoint) {
        try {
            this.endpoint = new URI(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    public HttpMethod getMethod() {
        return this.method;
    }

    public RARRequestMessageBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    public RARRequestMessageBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RARRequestMessageBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    public RARRequestMessageBuilder setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public RARRequestMessageBuilder addParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public RARRequestMessageBuilder setInputStream(InputStream instream) {
        this.inputStream = instream;
        return this;
    }


    public long getInputSize() {
        return this.inputSize;
    }

    public RARRequestMessageBuilder setInputSize(long inputSize) {
        CodingUtils.assertParameterInRange(inputSize, -1L, 5368709120L);
        this.inputSize = inputSize;
        return this;
    }

    public boolean isUseChunkEncoding() {
        return this.useChunkEncoding;
    }

    public RARRequestMessageBuilder setUseChunkEncoding(boolean useChunkEncoding) {
        this.useChunkEncoding = useChunkEncoding;
        return this;
    }

    public RARRequestMessageBuilder setOriginalRequest(WebServiceRequest originalRequest) {
        this.originalRequest = originalRequest;
        return this;
    }

    public RequestMessage build() {
        ClientConfiguration clientCofig = this.innerClient.getConfig();
        Map<String, String> sentHeaders = new HashMap(this.headers);
        Map<String, String> sentParameters = new LinkedHashMap(this.parameters);
        Date now = new Date();
        if (clientCofig.getTickOffset() != 0L) {
            now.setTime(now.getTime() + clientCofig.getTickOffset());
        }

        sentHeaders.put("Date", DateUtil.formatRfc822Date(now));
        RequestMessage request = new RequestMessage(this.originalRequest);
        request.setEndpoint(this.endpoint);
        request.setHeaders(sentHeaders);
        request.setParameters(sentParameters);
        request.setHttpMethod(this.method);
        request.setContent(this.inputStream);
        request.setContentLength(this.inputSize);
        request.setUseChunkEncoding(this.inputSize == -1L ? true : this.useChunkEncoding);
        return request;
    }

}
