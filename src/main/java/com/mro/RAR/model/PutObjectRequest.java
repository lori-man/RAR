package com.mro.RAR.model;

import com.mro.RAR.common.utils.DateUtil;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class PutObjectRequest<T> extends WebServiceRequest{
    // uri params
    private Map<String, String> uriParams;

    //请求 headers
    private Map<String, String> headers;

    private T metadata;


    public PutObjectRequest() {
        this.uriParams = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    }

    public PutObjectRequest(T metadata) {
        this.uriParams = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.metadata = metadata;
    }


    public Map<String, String> getUriParams() {
        return this.uriParams;
    }

    public void setUriParams(Map<String, String> uriParams) {
        this.uriParams.clear();
        if (uriParams != null && !uriParams.isEmpty()) {
            this.uriParams.putAll(uriParams);
        }

    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public T getMetadata() {
        return metadata;
    }

    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addUserMetadata(String key, String value) {
        this.uriParams.put(key, value);
    }

    public String getLastModified() {
        return this.headers.get("Last-Modified");
    }

    public void setLastModified(Date lastModified) {
        this.headers.put("Last-Modified", lastModified.toString());
    }

    public Date getExpirationTime() throws ParseException {
        String expires = (String)this.headers.get("Expires");
        return expires != null ? DateUtil.parseRfc822Date((String)this.headers.get("Expires")) : null;
    }

    public String getRawExpiresValue() {
        return (String)this.headers.get("Expires");
    }

    public void setExpirationTime(Date expirationTime) {
        this.headers.put("Expires", DateUtil.formatRfc822Date(expirationTime));
    }

    public long getContentLength() {
        Long contentLength = Long.parseLong(this.headers.get("Content-Length"));
        return contentLength == null ? 0L : contentLength;
    }

    public void setContentLength(long contentLength) {
        this.headers.put("Content-Length", String.valueOf(contentLength));
    }

    public String getContentType() {
        return (String)this.headers.get("Content-Type");
    }

    public void setContentType(String contentType) {
        this.headers.put("Content-Type", contentType);
    }

    public String getContentMD5() {
        return (String)this.headers.get("Content-MD5");
    }

    public void setContentMD5(String contentMD5) {
        this.headers.put("Content-MD5", contentMD5);
    }

    public String getContentEncoding() {
        return (String)this.headers.get("Content-Encoding");
    }

    public void setContentEncoding(String encoding) {
        this.headers.put("Content-Encoding", encoding);
    }

    public String getCacheControl() {
        return (String)this.headers.get("Cache-Control");
    }

    public void setCacheControl(String cacheControl) {
        this.headers.put("Cache-Control", cacheControl);
    }

    public String getContentDisposition() {
        return (String)this.headers.get("Content-Disposition");
    }

    public void setContentDisposition(String disposition) {
        this.headers.put("Content-Disposition", disposition);
    }

    public String getObjectType() {
        return (String)this.headers.get("x-RAR-object-type");
    }


    public Map<String, String> getRawMetadata() {
        return Collections.unmodifiableMap(this.headers);
    }

}
