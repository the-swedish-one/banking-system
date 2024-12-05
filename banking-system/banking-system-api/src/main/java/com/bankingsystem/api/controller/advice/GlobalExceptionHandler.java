package com.bankingsystem.api.controller.advice;

import com.bankingsystem.api.model.ErrorResponse;
import com.bankingsystem.domain.exception.InsufficientFundsException;
import com.bankingsystem.domain.exception.OverdraftLimitExceededException;
import com.bankingsystem.persistence.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    Project specific exceptions
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex, HttpServletRequest request) {
        logger.error("Insufficient Funds Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverdraftLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleOverdraftLimitExceededException(OverdraftLimitExceededException ex, HttpServletRequest request) {
        logger.error("Overdraft Limit Exceeded Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex, HttpServletRequest request) {
        logger.error("Account Not Found Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonDetailsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonDetailsNotFoundException(PersonDetailsNotFoundException ex, HttpServletRequest request) {
        logger.error("Person Details Not Found Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex, HttpServletRequest request) {
        logger.error("Transaction Not Found Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        logger.error("User Not Found Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BankNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBankNotFoundException(BankNotFoundException ex, HttpServletRequest request) {
        logger.error("Bank Not Found Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

//    Generic exceptions
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        logger.error("Illegal Argument Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Invalid input: " + ex.getMessage(),
                request.getRequestURI())
                ;
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex, WebRequest request) {
        logger.error("HTTP Message Not Readable Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_JSON",
                "Request body is malformed",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Generic Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "An error occurred: " + ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
