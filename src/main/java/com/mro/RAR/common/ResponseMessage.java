package com.mro.RAR.common;

import com.mro.RAR.ServiceClient;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class ResponseMessage extends HttpMesssage {
    private static final int HTTP_SUCCESS_STATUS_CODE = 200;
    private String uri;
    private int statusCode;
    private ServiceClient.Request request;
    private CloseableHttpResponse httpResponse;
    private String errorResponseAsString;

    public ResponseMessage(ServiceClient.Request request) {
        this.request = request;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUrl(String uri) {
        this.uri = uri;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getrequestUri() {
        return this.request.getUri();
    }

    public ServiceClient.Request getRequest() {
        return this.request;
    }

    public boolean isSuccessful() {
        return this.statusCode / 100 == 2;
    }

    public String getErrorResponseAsString() {
        return this.errorResponseAsString;
    }

    public void setErrorResponseAsString(String errorResponseAsString) {
        this.errorResponseAsString = errorResponseAsString;
    }

    public void abort() throws IOException {
        if (this.httpResponse != null) {
            this.httpResponse.close();
        }

    }

    public CloseableHttpResponse getHttpResponse() {
        return this.httpResponse;
    }

    public void setHttpResponse(CloseableHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }
}
