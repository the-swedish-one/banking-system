package com.bankingsystem.models;

import com.bankingsystem.models.exceptions.OverdraftLimitExceededException;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        super(owner, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw Failed: Amount must be greater than 0");
        }
        if (this.balance + this.overdraftLimit >= amount) {
            this.balance -= amount;
        } else {
            throw new OverdraftLimitExceededException("Withdraw Failed: Overdraft limit exceeded");
        }
    }

    public double getOverdraftLimit() {
        return this.overdraftLimit;
    }

    public void setOverdraftLimit(double newOverdraftLimit) {
        this.overdraftLimit = newOverdraftLimit;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "iban=" + iban + '\'' +
                "owner=" + owner + '\'' +
                "overdraftLimit=" + overdraftLimit + '\'' +
                '}';
    }

}
