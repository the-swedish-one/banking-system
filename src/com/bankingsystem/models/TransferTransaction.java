package com.bankingsystem.models;

import java.util.UUID;

public class TransferTransaction extends Transaction {

    private String fromAccountId;
    private String toAccountId;

    public TransferTransaction(double amount, String fromAccountId, String toAccountId) {
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
}
