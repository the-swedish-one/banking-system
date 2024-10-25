package com.bankingsystem.models;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositTransaction extends Transaction {

    public DepositTransaction(BigDecimal amount) {
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
