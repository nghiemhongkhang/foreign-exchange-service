package com.nhk.fx.exception;

public class BusinessException extends RuntimeException {
    private final int status;
    private final String error;

    public BusinessException(int status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
