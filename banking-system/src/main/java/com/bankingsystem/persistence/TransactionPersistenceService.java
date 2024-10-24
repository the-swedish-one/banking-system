package com.bankingsystem.persistence;

import com.bankingsystem.models.Transaction;

import java.util.List;

public interface TransactionPersistenceService {

    // Create a new transaction
    Transaction save(Transaction transaction);

    // Get one transaction by ID
    Transaction getTransactionById(String transactionId);

    // Get all transactions
    List<Transaction> getAllTransactions();

    // Update transaction
    void updateTransaction(Transaction transaction);

    // Delete an transaction
    boolean deleteTransaction(String transactionId);
}
