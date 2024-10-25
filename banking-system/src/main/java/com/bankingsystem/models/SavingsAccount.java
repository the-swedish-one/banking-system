package com.bankingsystem.models;

import com.bankingsystem.models.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

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

    public double getInterestRatePercentage() {
        return this.interestRatePercentage;
    }

    public void setInterestRatePercentage(double newInterestRatePercentage) {
        this.interestRatePercentage = newInterestRatePercentage;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "iban=" + iban + '\'' +
                "owner=" + owner + '\'' +
                "interestRatePercentage=" + interestRatePercentage + '\'' +
                '}';
    }
}

