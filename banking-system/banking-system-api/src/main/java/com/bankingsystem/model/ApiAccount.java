package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ApiAccount {

    protected int accountId;
    protected String iban;
    protected String accountName;
    protected ApiUser owner;
    protected BigDecimal balance;
    protected CurrencyCode currency;

    public ApiAccount(ApiUser owner, BigDecimal balance, CurrencyCode currency) {
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
    }

}
