package com.bankingsystem.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonDetailsNotFoundException extends RuntimeException {
    public PersonDetailsNotFoundException(String message) {
        super(message);
    }
}
