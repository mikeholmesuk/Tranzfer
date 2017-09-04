package com.tech.rev.exception;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class WebException extends RuntimeException {
    // We'll assume we're not handling SIP so no need to support 6xx errors
    @NotNull
    @Size(min = 300, max = 599)
    private int statusCode;

    public WebException() {
        this(500);
    }

    public WebException(int statusCode) {
        this(statusCode, "There was an error when handling your request", null);
    }

    public WebException(int statusCode, String exceptionMessage) {
        this(statusCode, exceptionMessage, null);
    }

    public WebException(int statusCode, String exceptionMessage, Throwable throwable) {
        super(exceptionMessage, throwable);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
