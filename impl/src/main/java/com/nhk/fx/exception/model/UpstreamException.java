package com.nhk.fx.exception.model;

public class UpstreamException extends RuntimeException {
    public UpstreamException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
