package com.bankingsystem.models.exceptions;

public class PersonDetailsNotFoundException extends RuntimeException {
    public PersonDetailsNotFoundException(String message) {
        super(message);
    }
}
