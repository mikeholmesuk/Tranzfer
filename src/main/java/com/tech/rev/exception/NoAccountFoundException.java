package com.tech.rev.exception;

public class NoAccountFoundException extends WebException {
    public NoAccountFoundException() {
        super(404, "No `Account` found with the requested properties");
    }

    public NoAccountFoundException(String errorMessage) {
        super(404, errorMessage);
    }
}
