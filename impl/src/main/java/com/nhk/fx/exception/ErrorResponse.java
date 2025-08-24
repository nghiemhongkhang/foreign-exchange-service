package com.nhk.fx.exception;

public class ErrorResponse {
    public String timestamp;
    public int status;
    public String error;
    public String message;
    public String path;

    public ErrorResponse() {}

    public ErrorResponse(String timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public static ErrorResponse of(int status, String error, String message, String path) {
        var r = new ErrorResponse();
        r.timestamp = java.time.OffsetDateTime.now().toString();
        r.status = status;
        r.error = error;
        r.message = message;
        r.path = path;
        return r;
    }
}
