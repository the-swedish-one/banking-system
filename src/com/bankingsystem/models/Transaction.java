package com.bankingsystem.models;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId + '\'' +
                "amount=" + amount + '\'' +
                "timestamp=" + timestamp + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        // same object
        if (this == obj) return true;
        // Null or not the same class
        if (obj == null || getClass() != obj.getClass()) return false;

        Transaction transaction = (Transaction) obj;
        // Compare fields for logical equality
        return Objects.equals(transactionId, transaction.transactionId) &&
                Objects.equals(amount, transaction.amount) &&
                Objects.equals(timestamp, transaction.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, amount, timestamp);
    }
}
