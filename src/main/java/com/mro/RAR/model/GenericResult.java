package com.mro.RAR.model;

import com.mro.RAR.common.ResponseMessage;

/**
 * A generic result that contains some basic response options, such as
 * requestId.
 */
public abstract class GenericResult {
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getClientCRC() {
        return clientCRC;
    }

    public void setClientCRC(Long clientCRC) {
        this.clientCRC = clientCRC;
    }

    public Long getServerCRC() {
        return serverCRC;
    }

    public void setServerCRC(Long serverCRC) {
        this.serverCRC = serverCRC;
    }

    public ResponseMessage getResponse() {
        return response;
    }

    public void setResponse(ResponseMessage response) {
        this.response = response;
    }

    private String requestId;
    private Long clientCRC;
    private Long serverCRC;
    ResponseMessage response;
}
