package com.bankingsystem.api.model;

import com.bankingsystem.persistence.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCheckingAccount {

    private int accountId;
    private String iban;
    private String accountName;
    private ApiUser owner;
    private BigDecimal balance;
    private CurrencyCode currency;
    private BigDecimal overdraftLimit;
    private Instant overdraftTimestamp;

    public ApiCheckingAccount(ApiUser owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
    }
}
