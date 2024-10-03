package com.bankingsystem.models;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String iban, User owner, double balance, CurrencyCode currency, double interestRate) {
        super(iban, owner, balance, currency);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new Exception("Insufficient funds"); // TODO - create custom unchecked exception
        }
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double newInterestRate) {
        this.interestRate = newInterestRate;
    }
}

