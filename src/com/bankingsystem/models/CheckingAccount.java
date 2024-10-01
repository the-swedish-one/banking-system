package com.bankingsystem.models;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        super(accountId, IBAN, owner, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

//    @Override
//    public void withdraw(double amount) throws Exception {
//        if (balance + overdraftLimit >= amount) {
//            balance -= amount;
//        } else {
//            throw new Exception("Overdraft limit exceeded");
//        }
//    }

    public double getOverdraftLimit() {
        return this.overdraftLimit;
    }

    public void setOverdraftLimit(double newOverdraftLimit) {
        this.overdraftLimit = newOverdraftLimit;
    }

}
