package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiDepositTransaction extends ApiTransaction {

    private int toAccountId;

    public ApiDepositTransaction(BigDecimal amount, int toAccountId) {
        super(amount);
        this.toAccountId = toAccountId;
    }

}
