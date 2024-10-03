package com.bankingsystem.models;

import com.bankingsystem.models.exceptions.InsufficientFundsException;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(User owner, double balance, CurrencyCode currency, double interestRate) {
        super(owner, balance, currency);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double newInterestRate) {
        this.interestRate = newInterestRate;
    }
}

