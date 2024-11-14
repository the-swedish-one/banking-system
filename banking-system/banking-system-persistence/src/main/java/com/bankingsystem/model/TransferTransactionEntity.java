package com.bankingsystem.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class TransferTransactionEntity extends TransactionEntity {

    private int fromAccountId;
    private int toAccountId;

    public TransferTransactionEntity(BigDecimal amount, int fromAccountId, int toAccountId) {
        super(amount);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }
}
