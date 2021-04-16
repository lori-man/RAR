package com.mro.RAR.model;

import java.io.InputStream;

/**
 * 这是在多部分上传中上传文件部分的请求类。
 */
public class UploadPartRequest extends WebServiceRequest {
    private String uploadId;

    private String key;

    private int partNumber;

    private long partSize = -1;

    private String md5Digest;

    private InputStream inputStream;

    private boolean useChunkEncoding = false;

    // Traffic limit speed, its uint is bit/s (unused)
    private int trafficLimit;

    public UploadPartRequest() {
    }

    public UploadPartRequest(String key) {
        this.key = key;
    }

    public UploadPartRequest(String key, String uploadId, int partNumber, InputStream inputStream,
                             long partSize) {
        this.key = key;
        this.uploadId = uploadId;
        this.partNumber = partNumber;
        this.inputStream = inputStream;
        this.partSize = partSize;
    }


    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getMd5Digest() {
        return md5Digest;
    }

    public void setMd5Digest(String md5Digest) {
        this.md5Digest = md5Digest;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean isUseChunkEncoding() {
        return useChunkEncoding;
    }

    public void setUseChunkEncoding(boolean useChunkEncoding) {
        this.useChunkEncoding = useChunkEncoding;
    }

    public int getTrafficLimit() {
        return trafficLimit;
    }

    public void setTrafficLimit(int trafficLimit) {
        this.trafficLimit = trafficLimit;
    }
}
