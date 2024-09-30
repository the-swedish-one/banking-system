package com.bankingsystem.models;

public class JointCheckingAccount extends CheckingAccount {
    private User secondOwner;

    public JointCheckingAccount(int accountId, String IBAN, User owner, User secondOwner, double balance, CurrencyCode currency, double overdraftLimit) {
        super(accountId, IBAN, owner, balance, currency, overdraftLimit);
        this.secondOwner = secondOwner;
    }

    public User getSecondOwner() {
        return this.secondOwner;
    }

    public void setSecondOwner(User newSecondOwner) {
        this.secondOwner = newSecondOwner;
    }
}
