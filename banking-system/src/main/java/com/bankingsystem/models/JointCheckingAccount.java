package com.bankingsystem.models;

public class JointCheckingAccount extends CheckingAccount {
    private User secondOwner;

    public JointCheckingAccount(User owner, User secondOwner, double balance, CurrencyCode currency, double overdraftLimit) {
        super(owner, balance, currency, overdraftLimit);
        this.secondOwner = secondOwner;
    }

    public User getSecondOwner() {
        return this.secondOwner;
    }

    public void setSecondOwner(User newSecondOwner) {
        this.secondOwner = newSecondOwner;
    }

    @Override
    public String toString() {
        return "JointCheckingAccount{" +
                "iban=" + iban + '\'' +
                "owner=" + owner.getPerson().getFirstName() + owner.getPerson().getLastName() + '\'' +
                "secondOwner=" + secondOwner.getPerson().getFirstName() + secondOwner.getPerson().getLastName() + '\'' +
                '}';
    }
}
