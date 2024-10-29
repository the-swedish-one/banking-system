package com.bankingsystem.model;

import java.math.BigDecimal;
import java.util.UUID;

public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction( BigDecimal amount) {
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
