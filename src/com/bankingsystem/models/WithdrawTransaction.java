package com.bankingsystem.models;

import java.util.UUID;

public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction( double amount) {
        super(amount);
        this.transactionId = "withdraw-" + UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "WithdrawTransaction{" +
                "amount=" + amount + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
