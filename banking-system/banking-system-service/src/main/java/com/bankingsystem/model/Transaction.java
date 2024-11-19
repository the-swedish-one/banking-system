package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private int transactionId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private Integer fromAccountId;
    private Integer toAccountId;

    public Transaction(BigDecimal amount, Integer fromAccountId, Integer toAccountId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (fromAccountId == null && toAccountId == null) {
            throw new IllegalArgumentException("At least one of fromAccountId or toAccountId must be specified");
        }
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }
}
