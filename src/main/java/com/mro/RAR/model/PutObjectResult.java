package com.mro.RAR.model;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * The result class of a Put Object request.
 */
public class PutObjectResult extends GenericResult implements Closeable {
    // Object key (name)
    private String key;

    // Object's metadata.
    private ObjectMetadata metadata = new ObjectMetadata();

    // Object's content
    private InputStream objectContent;

    /**
     * Gets the object's metadata
     *
     * @return Object's metadata in（{@link ObjectMetadata}
     */
    public ObjectMetadata getObjectMetadata() {
        return metadata;
    }

    /**
     * Sets the object's metadata.
     *
     * @param metadata
     *            Object's metadata.
     */
    public void setObjectMetadata(ObjectMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get's the object's content in {@link InputStream}.
     *
     * @return The object's content in {@link InputStream}.
     */
    public InputStream getObjectContent() {
        return objectContent;
    }

    /**
     * Sets the object's content in {@link InputStream}.
     *
     * @param objectContent
     *            The object's content in {@link InputStream}.
     */
    public void setObjectContent(InputStream objectContent) {
        this.objectContent = objectContent;
    }

    /**
     * Gets the object's key.
     *
     * @return Object Key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the object's key.
     *
     * @param key
     *            Object Key.
     */
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void close() throws IOException {
        if (objectContent != null) {
            objectContent.close();
        }
    }

    /**
     * Forcefully close the response. The remaining data in the server will not
     * be downloaded.
     *
     * @throws IOException
     */
    public void forcedClose() throws IOException {
        this.response.abort();
    }

    @Override
    public String toString() {
        return "OSSObject [key=" + getKey()+"]";
    }
}
