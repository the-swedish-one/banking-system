package com.bankingsystem.services;

import com.bankingsystem.models.Account;

public class DepositTransaction extends Transaction {
    private Account account;

    public DepositTransaction(String transactionId, double amount, Account account) {
        super(transactionId, amount);
        this.account = account;
    }

    @Override
    public void execute() {
        try {
            account.deposit(amount);
            account.getTransactionHistory().add(this);
            System.out.println("Success! Amount deposited: " + amount);
        } catch (Exception e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    public Account getAccount() {
        return account;
    }
}
