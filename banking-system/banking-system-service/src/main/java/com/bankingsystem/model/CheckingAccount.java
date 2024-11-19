package com.bankingsystem.model;

import com.bankingsystem.exception.OverdraftLimitExceededException;
import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccount implements Withdrawable, Depositable  {

    private int accountId;
    private String iban;
    private String accountName;
    private User owner;
    private BigDecimal balance;
    private CurrencyCode currency;
    private BigDecimal overdraftLimit;

    public CheckingAccount(User owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        this.owner = owner;
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
    }

}
