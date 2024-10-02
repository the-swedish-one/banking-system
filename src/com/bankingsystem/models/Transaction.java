package com.bankingsystem.models;

import java.time.LocalDateTime;

public abstract class Transaction {
    protected String transactionId;
    protected double amount;
    protected LocalDateTime timestamp;

    public Transaction(String transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public abstract void execute();

    // Getters
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
