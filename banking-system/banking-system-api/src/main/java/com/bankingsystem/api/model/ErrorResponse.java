package com.bankingsystem.api.model;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Instant timestamp = Instant.now();

    private String errorCode;
    private String message;
    private String path;

    public ErrorResponse(String errorCode, String message, String path) {
        this.timestamp = Instant.now();
        this.errorCode = String.valueOf(errorCode);
        this.message = message;
        this.path = path;
    }
}
