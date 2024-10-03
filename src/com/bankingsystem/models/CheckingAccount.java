package com.bankingsystem.models;

import com.bankingsystem.models.exceptions.OverdraftLimitExceededException;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String iban, User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        super(iban, owner, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (this.balance + this.overdraftLimit >= amount) {
            this.balance -= amount;
        } else {
            throw new OverdraftLimitExceededException("Overdraft limit exceeded");
        }
    }

    public double getOverdraftLimit() {
        return this.overdraftLimit;
    }

    public void setOverdraftLimit(double newOverdraftLimit) {
        this.overdraftLimit = newOverdraftLimit;
    }

}
