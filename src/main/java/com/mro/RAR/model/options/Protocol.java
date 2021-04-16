package com.mro.RAR.model.options;

public enum Protocol {
    HTTP("http"),
    HTTPS("https");

    private final String protocol;

    private Protocol(String protocol) {
        this.protocol = protocol;
    }

    public String toString() {
        return this.protocol;
    }
}
