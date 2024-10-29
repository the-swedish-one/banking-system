package com.bankingsystem.model;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferTransaction extends Transaction {

    private String fromAccountId;
    private String toAccountId;

    public TransferTransaction(BigDecimal amount, String fromAccountId, String toAccountId) {
        super(amount);
        this.transactionId = "transfer-" + UUID.randomUUID();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    public String getFromAccountId() {
        return this.fromAccountId;
    }

    public String getToAccountId() {
        return this.toAccountId;
    }

    @Override
    public String toString() {
        return "TransferTransaction{" +
                "amount=" + amount +
                ", fromAccountId='" + fromAccountId + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
