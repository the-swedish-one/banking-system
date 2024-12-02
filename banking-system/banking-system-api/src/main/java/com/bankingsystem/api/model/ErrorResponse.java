package com.bankingsystem.api.model;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private ZonedDateTime timestamp = ZonedDateTime.now();

    private String errorCode;
    private String message;
    private String path;

    public ErrorResponse(String errorCode, String message, String path) {
        this.timestamp = ZonedDateTime.now();
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }
}
