package com.bankingsystem.services;


import com.bankingsystem.models.*;
import com.bankingsystem.persistence.TransactionPersistenceService;

import java.util.List;

public class TransactionService {

    private final TransactionPersistenceService transactionPersistenceService;

    public TransactionService(TransactionPersistenceService transactionPersistenceService) {
        this.transactionPersistenceService = transactionPersistenceService;
    }

    // Create new deposit transaction
    public void createDepositTransaction(double amount) {
        DepositTransaction transaction = new DepositTransaction(amount);
        transactionPersistenceService.createTransaction(transaction);
    }

    // Create new withdraw transaction
    public void createWithdrawTransaction(double amount) {
        WithdrawTransaction transaction = new WithdrawTransaction(amount);
        transactionPersistenceService.createTransaction(transaction);
    }

    // Create new transfer transaction
    public void createTransferTransaction(double amount, String fromAccountId, String toAccountId) {
        TransferTransaction transaction = new TransferTransaction(amount, fromAccountId, toAccountId);
        transactionPersistenceService.createTransaction(transaction);
    }

    // Get transaction by ID
    public Transaction getTransactionById(String transactionId) {
        return transactionPersistenceService.getTransactionById(transactionId);
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionPersistenceService.getAllTransactions();
    }

    // Delete transaction by ID
    public boolean deleteTransactions(String transactionId) {
        return transactionPersistenceService.deleteTransaction(transactionId);
    }
}
