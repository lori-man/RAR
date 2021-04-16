package com.mro.RAR.model;

import java.io.InputStream;

/**
 * 断点续传返回结果
 */
public class UploadFileResult extends GenericResult {

    /**
     * The URL identifying the new multipart object.
     */
    private String location;

    private String eTag;

    /**
     * Object Version Id.
     */
    private String versionId;

    /**
     * The callback request's response body
     */
    private InputStream callbackResponseBody;

    /**
     * Gets the url of the target file of this multipart upload.
     *
     * @return The url of the target file of this multipart upload.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the url of the target file of this multipart upload.
     *
     * @param location The url of the target file of this multipart upload.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the ETag of the target file.
     *
     * @return ETag of the target file.
     */
    public String getETag() {
        return eTag;
    }

    /**
     * Sets the ETag of the target file.
     *
     * @param etag ETag of the target file.
     */
    public void setETag(String etag) {
        this.eTag = etag;
    }

    /**
     * Gets version id.
     *
     * @return version id.
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * Sets version id.
     *
     * @param versionId
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
     * Deprecated. Gets the callback response body. The caller needs to close it
     * after usage.
     *
     * @return The response body.
     */
    @Deprecated
    public InputStream getCallbackResponseBody() {
        return callbackResponseBody;
    }

    /**
     * Sets the callback response body.
     *
     * @param callbackResponseBody The callback response body.
     */
    public void setCallbackResponseBody(InputStream callbackResponseBody) {
        this.callbackResponseBody = callbackResponseBody;
    }

}
