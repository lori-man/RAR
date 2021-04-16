package com.mro.RAR.model;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;


public class RARObject implements Closeable {

    // Object key (name)
    private String key;

    // Object's metadata.
    private ObjectMetadata metadata = new ObjectMetadata();

    // Object's content
    private InputStream objectContent;

    @Override
    public void close() throws IOException {

    }
}
