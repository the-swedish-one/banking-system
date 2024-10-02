package com.bankingsystem.models;

import java.util.List;

public abstract class Account implements Withdrawable {
    protected int accountId;
    protected String iban;
    protected String accountName;
    protected User owner;
    protected double balance;
    protected CurrencyCode currency;
    protected List<Transaction> transactionHistory;

    public Account(int accountId, String iban, User owner, double balance, CurrencyCode currency) {
        this.accountId = accountId;
        this.iban = iban;
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
    }

    public abstract void withdraw(double amount) throws Exception;

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int newAccountId) {
        this.accountId = newAccountId;
    }

    public String getIban() {
        return this.iban;
    }

    public void setIban(String newIBAN) {
        this.iban = newIBAN;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String newAccountName) {
        this.accountName = newAccountName;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User newOwner) {
        this.owner = newOwner;
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

