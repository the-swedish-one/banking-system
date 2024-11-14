package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositTransaction extends Transaction {

    private int toAccountId;

    public DepositTransaction(BigDecimal amount, int toAccountId) {
        super(amount);
        this.toAccountId = toAccountId;
    }

}
