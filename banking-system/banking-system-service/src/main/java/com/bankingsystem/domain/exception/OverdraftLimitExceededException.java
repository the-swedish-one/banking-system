package com.bankingsystem.domain.exception;

public class OverdraftLimitExceededException extends RuntimeException {
    public OverdraftLimitExceededException(String message) {
        super(message);
    }
}
