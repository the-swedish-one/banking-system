package com.bankingsystem.models;

public class JointAccount extends Account {
    private User secondOwner;

    public JointAccount(int accountId, String IBAN, User owner, User secondOwner, double balance, CurrencyCode currency) {
        super(accountId, IBAN, owner, balance, currency);
        this.secondOwner = secondOwner;
    }

    public User getSecondOwner() {
        return this.secondOwner;
    }

    public void setSecondOwner(User newSecondOwner) {
        this.secondOwner = newSecondOwner;
    }
}
