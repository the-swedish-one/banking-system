package com.bankingsystem.models.exceptions;

public class OverdraftLimitExceededException extends RuntimeException {
    public OverdraftLimitExceededException(String message) {
        super(message);
    }
}
