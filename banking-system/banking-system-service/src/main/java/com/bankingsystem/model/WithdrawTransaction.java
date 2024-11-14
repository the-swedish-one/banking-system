package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawTransaction extends Transaction {

    private int fromAccountId;

    public WithdrawTransaction( BigDecimal amount, int fromAccountId) {
        super(amount);
        this.fromAccountId = fromAccountId;
    }

}
