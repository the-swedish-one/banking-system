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
    private String fromAccountIban;
    private String toAccountIban;

    public Transaction(BigDecimal amount, String fromAccountIban, String toAccountIban) {
        this.amount = amount;
        this.fromAccountIban = fromAccountIban;
        this.toAccountIban = toAccountIban;
    }
}
