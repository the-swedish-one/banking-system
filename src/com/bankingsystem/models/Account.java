package com.bankingsystem.models;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class Account implements Withdrawable, Depositable {
    protected String accountId;
    protected String iban;
    protected String accountName;
    protected User owner;
    protected double balance;
    protected CurrencyCode currency;
    protected List<Transaction> transactionHistory;

    public Account(User owner, double balance, CurrencyCode currency) {
        this.accountId = "account-" + UUID.randomUUID();
        this.iban = generateIBAN(owner.getPerson().getCountry());;
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
    }

    private String generateIBAN(String country) {
        // Get the first two letters of the country code
        String countryCode = country.substring(0, 2).toUpperCase();
        StringBuilder ibanBuilder = new StringBuilder(countryCode);

        // Generate 14 random digits
        Random random = new Random();
        for (int i = 0; i < 14; i++) {
            ibanBuilder.append(random.nextInt(10));
        }
        return ibanBuilder.toString();
    }

    @Override
    public void deposit(double amount) {
        this.balance = this.balance + amount;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String newAccountId) {
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

