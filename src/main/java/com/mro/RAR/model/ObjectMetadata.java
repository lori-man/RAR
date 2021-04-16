package com.mro.RAR.model;

import com.mro.RAR.common.utils.DateUtil;
import com.mro.RAR.common.utils.RARHeaders;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * RAR Object's metadata. It has the user's custom metadata, as well as some
 * standard http headers sent to OSS, such as Content-Length, ETag, etc.
 */
public class ObjectMetadata {
    // The user's custom metadata, whose prefix in http header is x-oss-meta-.
    private Map<String, String> userMetadata = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

    // Other non-custom metadata.
    protected Map<String, Object> metadata = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);

    public static final String AES_256_SERVER_SIDE_ENCRYPTION = "AES256";

    public static final String KMS_SERVER_SIDE_ENCRYPTION = "KMS";

    /**
     * Gets the user's custom metadata.
     *
     * 规则:不区分大小写,不应该使用x-rar-meta-作为前缀
     */
    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    /**
     * Sets the user's custom metadata.
     */
    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata.clear();
        if (userMetadata != null && !userMetadata.isEmpty()) {
            this.userMetadata.putAll(userMetadata);
        }
    }


    /**
     * Sets the http header (SDK internal usage only).
     */
    public void setHeader(String key, Object value) {
        metadata.put(key, value);
    }

    /**
     * Adds a new custom metadata.
     */
    public void addUserMetadata(String key, String value) {
        this.userMetadata.put(key, value);
    }

    /**
     * Gets the value of Last-Modified header, which means the last modified
     * time of the object. 最后一次修改时间
     */
    public Date getLastModified() {
        return (Date) metadata.get(RARHeaders.LAST_MODIFIED);
    }

    /**
     * Sets the value of Last-Modified header, which means the last modified
     * time of the object.
     */
    public void setLastModified(Date lastModified) {
        metadata.put(RARHeaders.LAST_MODIFIED, lastModified);
    }

    /**
     * Gets the {@link Date} value of the "Expires" header in Rfc822 format. If
     * expiration is not set, then the value is null. 非Rfc822格式
     */
    public Date getExpirationTime() throws ParseException {
        String expires = (String) metadata.get(RARHeaders.EXPIRES);

        if (expires != null)
            return DateUtil.parseRfc822Date((String) metadata.get(RARHeaders.EXPIRES));

        return null;
    }

    /**
     * Gets the string value of the "Expires" header in Rfc822 format. If
     * expiration is not set, then the value is null.
     */
    public String getRawExpiresValue() {
        return (String) metadata.get(RARHeaders.EXPIRES);
    }

    /**
     * Sets the "Expires" header.
     */
    public void setExpirationTime(Date expirationTime) {
        metadata.put(RARHeaders.EXPIRES, DateUtil.formatRfc822Date(expirationTime));
    }

    /**
     * Gets Content-Length header, which is the object content's size.
     */
    public long getContentLength() {
        Long contentLength = (Long) metadata.get(RARHeaders.CONTENT_LENGTH);
        return contentLength == null ? 0 : contentLength.longValue();
    }

    /**
     * Sets the Content-Length header to indicate the object's size. The correct
     * Content-Length header is needed for a file upload.
     */
    public void setContentLength(long contentLength) {
        metadata.put(RARHeaders.CONTENT_LENGTH, contentLength);
    }

    /**
     * Gets the Content-Type header to indicate the object content's type in
     * MIME type format.
     */
    public String getContentType() {
        return (String) metadata.get(RARHeaders.CONTENT_TYPE);
    }

    /**
     * Sets the Content-Type header to indicate the object content's type in
     * MIME type format.
     */
    public void setContentType(String contentType) {
        metadata.put(RARHeaders.CONTENT_TYPE, contentType);
    }

    public String getContentMD5() {
        return (String) metadata.get(RARHeaders.CONTENT_MD5);
    }

    public void setContentMD5(String contentMD5) {
        metadata.put(RARHeaders.CONTENT_MD5, contentMD5);
    }

    /**
     * Gets the Content-Encoding header which is to encode the object content.
     */
    public String getContentEncoding() {
        return (String) metadata.get(RARHeaders.CONTENT_ENCODING);
    }

    /**
     * Sets the Content-Encoding header which is to encode the object content.
     * 编码格式
     */
    public void setContentEncoding(String encoding) {
        metadata.put(RARHeaders.CONTENT_ENCODING, encoding);
    }

    /**
     * Gets the Cache-Control header. This is the standard http header.
     */
    public String getCacheControl() {
        return (String) metadata.get(RARHeaders.CACHE_CONTROL);
    }

    /**
     * Sets the Cache-Control header. This is the standard http header.
     */
    public void setCacheControl(String cacheControl) {
        metadata.put(RARHeaders.CACHE_CONTROL, cacheControl);
    }


    /**
     * Gets the ETag of the object. ETag is the 128bit MD5 signature in Hex.
     */
    public String getETag() {
        return (String) metadata.get(RARHeaders.ETAG);
    }

    /**
     * Gets the raw metadata (SDK internal usage only). The value returned is
     * immutable.
     */
    public Map<String, Object> getRawMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    /**
     * Gets the request Id.
     */
    public String getRequestId() {
        return (String) metadata.get(RARHeaders.RAR_HEADER_REQUEST_ID);
    }

    /**
     * Gets the version ID of the associated OSS object if available.
     * Version IDs are only assigned to objects when an object is uploaded to an
     * OSS bucket that has object versioning enabled.
     */
    public String getVersionId() {
        return (String)metadata.get(RARHeaders.RAR_HEADER_VERSION_ID);
    }

    /**
     * Gets the service crc.
     */
    public Long getServerCRC() {
        String strSrvCrc = (String) metadata.get(RARHeaders.RAR_HASH_CRC64_ECMA);

        if (strSrvCrc != null) {
            BigInteger bi = new BigInteger(strSrvCrc);
            return bi.longValue();
        }
        return null;
    }
}
