package com.bankingsystem.exception;

public class PersonDetailsNotFoundException extends RuntimeException {
    public PersonDetailsNotFoundException(String message) {
        super(message);
    }
}
