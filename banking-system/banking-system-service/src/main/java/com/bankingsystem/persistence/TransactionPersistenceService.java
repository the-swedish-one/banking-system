package com.bankingsystem.persistence;

import com.bankingsystem.model.TransactionEntity;

import java.util.List;

public interface TransactionPersistenceService {

    // Create a new transaction
    TransactionEntity save(TransactionEntity transaction);

    // Get one transaction by ID
    TransactionEntity getTransactionById(String transactionId);

    // Get all transactions
    List<TransactionEntity> getAllTransactions();

    // Update transaction
    void updateTransaction(TransactionEntity transaction);

    // Delete an transaction
    boolean deleteTransaction(String transactionId);
}
