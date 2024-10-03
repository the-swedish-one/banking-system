package com.bankingsystem.models;

import java.time.LocalDateTime;

public abstract class Transaction {
    protected String transactionId;
    protected double amount;
    protected LocalDateTime timestamp;

    public Transaction(double amount) {
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
