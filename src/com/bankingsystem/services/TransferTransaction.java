package com.bankingsystem.services;

import com.bankingsystem.models.Account;

public class TransferTransaction extends Transaction {
    private Account fromAccount;
    private Account toAccount;

    public TransferTransaction(String transactionId, double amount, Account fromAccount, Account toAccount) {
        super(transactionId, amount);
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    @Override
    public void execute() {
        try {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            fromAccount.getTransactionHistory().add(this);
            toAccount.getTransactionHistory().add(this);
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    public Account getFromAccount() {
        return this.fromAccount;
    }

    public Account getToAccount() {
        return this.toAccount;
    }
}
