package com.bankingsystem.models;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(int accountId, String IBAN, User owner1, User owner2, double balance, CurrencyCode currency, double interestRate) {
        super(accountId, IBAN, owner1, owner2, balance, currency);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        balance += balance * interestRate;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double newInterestRate) {
        this.interestRate = newInterestRate;
    }

}

