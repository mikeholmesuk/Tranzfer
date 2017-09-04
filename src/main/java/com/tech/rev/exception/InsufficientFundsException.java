package com.tech.rev.exception;

public class InsufficientFundsException extends WebException {
    public InsufficientFundsException() {
        super(400, "Debit Account has insufficient funds for this transfer");
    }

    public InsufficientFundsException(String errorMessage) {
        super(400, errorMessage);
    }
}
