package com.bankingsystem.models;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double interestRate) {
        super(accountId, IBAN, owner, balance, currency);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new Exception("Insufficient funds");
        }
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double newInterestRate) {
        this.interestRate = newInterestRate;
    }
}

