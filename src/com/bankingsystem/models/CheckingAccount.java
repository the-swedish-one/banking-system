package com.bankingsystem.models;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String iban, User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        super(iban, owner, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (this.balance + this.overdraftLimit >= amount) {
            this.balance -= amount;
        } else {
            throw new Exception("Overdraft limit exceeded"); // TODO - create custom unchecked exception
        }
    }

    public double getOverdraftLimit() {
        return this.overdraftLimit;
    }

    public void setOverdraftLimit(double newOverdraftLimit) {
        this.overdraftLimit = newOverdraftLimit;
    }

}
