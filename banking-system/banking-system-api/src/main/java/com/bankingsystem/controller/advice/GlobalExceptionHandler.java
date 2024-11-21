package com.bankingsystem.controller.advice;

import com.bankingsystem.exception.*;
import com.bankingsystem.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Insufficient funds: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverdraftLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleOverdraftLimitExceededException(OverdraftLimitExceededException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Overdraft limit exceeded: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "Account not found: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonDetailsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonDetailsNotFoundException(PersonDetailsNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "Person Details not found: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "Transaction not found: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "User not found: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BankNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBankNotFoundException(BankNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "Bank not found: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid input: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "An error occurred: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
