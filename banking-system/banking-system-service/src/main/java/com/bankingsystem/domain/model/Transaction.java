package com.bankingsystem.domain.model;

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
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }
}
