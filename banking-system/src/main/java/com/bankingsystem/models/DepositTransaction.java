package com.bankingsystem.models;

import java.util.UUID;

public class DepositTransaction extends Transaction {

    public DepositTransaction(double amount) {
        super(amount);
        this.transactionId = "deposit-" + UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "DepositTransaction{" +
                "amount=" + amount + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
