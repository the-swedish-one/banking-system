package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Transaction {

    protected int transactionId;
    protected BigDecimal amount;
    protected LocalDateTime timestamp;

    public Transaction(BigDecimal amount) {
        this.amount = amount;
    }
}
