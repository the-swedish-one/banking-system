package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCheckingAccount extends ApiAccount {

    private BigDecimal overdraftLimit;

    public ApiCheckingAccount(ApiUser owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        super(owner, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }
}
