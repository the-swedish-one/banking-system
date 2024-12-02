package com.bankingsystem.api.model;

import com.bankingsystem.persistence.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSavingsAccount {

    private int accountId;
    private String iban;
    private String accountName;
    private ApiUser owner;
    private BigDecimal balance;
    private CurrencyCode currency;
    private double interestRatePercentage;

    public ApiSavingsAccount(ApiUser owner, BigDecimal balance, CurrencyCode currency, double interestRate) {
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
        this.interestRatePercentage = interestRate;
    }

}

