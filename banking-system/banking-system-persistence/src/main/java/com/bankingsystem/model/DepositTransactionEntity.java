package com.bankingsystem.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class DepositTransactionEntity extends TransactionEntity {

    private int toAccountId;

    public DepositTransactionEntity(BigDecimal amount, int toAccountId) {
        super(amount);
        this.toAccountId = toAccountId;
    }

}
