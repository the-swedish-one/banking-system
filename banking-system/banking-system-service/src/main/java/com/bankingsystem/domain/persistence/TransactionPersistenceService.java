package com.bankingsystem.domain.persistence;

import com.bankingsystem.domain.model.Transaction;

import java.util.List;

public interface TransactionPersistenceService {

    // Create a new transaction
    Transaction save(Transaction depositTransaction);


    // Get one transaction by ID
    Transaction getTransactionById(int transactionId);

    // Get all transactions
    List<Transaction> getAllTransactions();

    // Update transaction
    Transaction updateTransaction(Transaction transaction);

    // Delete an transaction
    boolean deleteTransaction(int transactionId);
}
