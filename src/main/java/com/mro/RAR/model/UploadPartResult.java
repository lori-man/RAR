package com.mro.RAR.model;

import com.mro.RAR.common.ResponseMessage;

/**
 *  multipart upload 中一部分上传返回的result
 */
public class UploadPartResult {

    private String requestId;
    private Long clientCRC;
    private Long serverCRC;
    ResponseMessage response;

    private int partNumber;
    private long partSize;
    private String eTag;


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

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public long getPartSize() {
        return partSize;
    }

    public void setPartSize(long partSize) {
        this.partSize = partSize;
    }

    public String getETag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
