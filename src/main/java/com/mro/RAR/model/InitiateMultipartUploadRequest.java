package com.mro.RAR.model;

/**
 * 这是用于发起多部分上传的请求
 */
public class InitiateMultipartUploadRequest extends WebServiceRequest {

    private String key;

    private ObjectMetadata objectMetadata;

    public InitiateMultipartUploadRequest(String key) {
        this.key = key;
    }

    public InitiateMultipartUploadRequest(String key, ObjectMetadata objectMetadata) {
        this.key = key;
        this.objectMetadata = objectMetadata;
    }

    public ObjectMetadata getObjectMetadata() {
        return objectMetadata;
    }

    public void setObjectMetadata(ObjectMetadata objectMetadata) {
        this.objectMetadata = objectMetadata;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
