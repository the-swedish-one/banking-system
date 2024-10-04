package com.bankingsystem.models;

import com.bankingsystem.models.exceptions.InsufficientFundsException;

public class SavingsAccount extends Account {
    private double interestRatePercentage;

    public SavingsAccount(User owner, double balance, CurrencyCode currency, double interestRate) {
        super(owner, balance, currency);
        this.interestRatePercentage = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    public double getInterestRatPercentage() {
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

