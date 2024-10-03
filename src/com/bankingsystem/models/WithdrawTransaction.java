package com.bankingsystem.models;

import java.util.UUID;

public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction( double amount) {
        super(amount);
        this.transactionId = "withdraw-" + UUID.randomUUID();
    }
}
