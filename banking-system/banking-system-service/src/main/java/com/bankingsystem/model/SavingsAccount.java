package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.exception.InsufficientFundsException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccount implements Withdrawable, Depositable {

    private int accountId;
    private String iban;
    private String accountName;
    private User owner;
    private BigDecimal balance;
    private CurrencyCode currency;
    private double interestRatePercentage;

    public SavingsAccount(User owner, BigDecimal balance, CurrencyCode currency, double interestRate) {
        this.owner = owner;
        this.balance = balance;
        this.interestRatePercentage = interestRate;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw Failed: Amount must be greater than 0");
        }
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
        } else {
            throw new InsufficientFundsException("Withdraw Failed: Insufficient funds");
        }
    }

    @Override
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit failed: Amount must be greater than 0");
        }
        this.balance = this.balance.add(amount);
    }
}

