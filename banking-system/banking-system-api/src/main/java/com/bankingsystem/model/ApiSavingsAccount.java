package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSavingsAccount extends ApiAccount {

    private double interestRatePercentage;

    public ApiSavingsAccount(ApiUser owner, BigDecimal balance, CurrencyCode currency, double interestRate) {
        super(owner, balance, currency);
        this.interestRatePercentage = interestRate;
    }

}

