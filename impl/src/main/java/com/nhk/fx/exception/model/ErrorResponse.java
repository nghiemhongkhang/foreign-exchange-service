package com.nhk.fx.exception.model;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    public String timestamp;
    public HttpStatus status;
    public String message;
    public String path;

    public ErrorResponse() {}

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        var r = new ErrorResponse();
        r.timestamp = java.time.OffsetDateTime.now().toString();
        r.status = status;
        r.message = message;
        r.path = path;
        return r;
    }
}
