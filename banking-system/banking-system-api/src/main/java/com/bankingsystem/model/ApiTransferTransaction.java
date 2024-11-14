package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiTransferTransaction extends ApiTransaction {

    private int fromAccountId;
    private int toAccountId;

    public ApiTransferTransaction(BigDecimal amount, int fromAccountId, int toAccountId) {
        super(amount);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }
}
