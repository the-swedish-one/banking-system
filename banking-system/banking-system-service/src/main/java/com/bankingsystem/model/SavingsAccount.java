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
public class SavingsAccount extends Account {

    private double interestRatePercentage;

    public SavingsAccount(User owner, BigDecimal balance, CurrencyCode currency, double interestRate) {
        super(owner, balance, currency);
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
            throw new InsufficientFundsException("Withdraw Filed: Insufficient funds");
        }
    }
}

