package com.bankingsystem.models;

import com.bankingsystem.services.*;

import java.util.List;

public class Account {
    protected int accountId;
    protected String IBAN;
    protected String accountName;
    protected User owner1;
    protected User owner2;
    protected double balance;
    protected CurrencyCode currency;
    protected List<Transaction> transactionHistory;

    public Account(int accountId, String IBAN, User owner1, User owner2, double balance, CurrencyCode currency) {
        this.accountId = accountId;
        this.IBAN = IBAN;
        this.owner1 = owner1;
        this.owner2 = owner2;
        this.balance = balance;
        this.currency = currency;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) throws Exception {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new Exception("Insufficient funds");
        }
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int newAccountId) {
        this.accountId = newAccountId;
    }

    public String getIBAN() {
        return this.IBAN;
    }

    public void setIBAN(String newIBAN) {
        this.IBAN = newIBAN;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String newAccountName) {
        this.accountName = newAccountName;
    }

    public User getOwner1() {
        return this.owner1;
    }

    public void setOwner1(User newOwner1) {
        this.owner1 = newOwner1;
    }

    public User getOwner2() {
        return this.owner2;
    }

    public void setOwner2(User newOwner2) {
        this.owner2 = newOwner2;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public CurrencyCode getCurrency() {
        return this.currency;
    }

    public void setCurrency(CurrencyCode newCurrency) {
        this.currency = newCurrency;
    }

    public List<Transaction> getTransactionHistory() {
        return this.transactionHistory;
    }
}

