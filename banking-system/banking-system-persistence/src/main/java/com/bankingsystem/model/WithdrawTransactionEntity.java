package com.bankingsystem.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class WithdrawTransactionEntity extends TransactionEntity {

    private int fromAccountId;

    public WithdrawTransactionEntity(BigDecimal amount, int fromAccountId) {
        super(amount);
        this.fromAccountId = fromAccountId;
    }
}
