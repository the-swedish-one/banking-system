package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiWithdrawTransaction extends ApiTransaction {

    private int fromAccountId;

    public ApiWithdrawTransaction( BigDecimal amount, int fromAccountId) {
        super(amount);
        this.fromAccountId = fromAccountId;
    }

}
