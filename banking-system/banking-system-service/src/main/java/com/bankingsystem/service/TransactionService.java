package com.bankingsystem.service;

import com.bankingsystem.model.Transaction;
import com.bankingsystem.exception.TransactionNotFoundException;
import com.bankingsystem.persistence.TransactionPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionPersistenceService transactionPersistenceService;

    public TransactionService(TransactionPersistenceService transactionPersistenceService) {
        this.transactionPersistenceService = transactionPersistenceService;
    }

    // Create new transaction
    public Transaction createTransaction(Transaction transaction) {
        return transactionPersistenceService.save(transaction);
    }

    // Get transaction by ID
    public Transaction getTransactionById(int transactionId) {
        if (transactionId <= 0) {
            throw new IllegalArgumentException("Transaction ID must be greater than zero");
        }
        return transactionPersistenceService.getTransactionById(transactionId);
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionPersistenceService.getAllTransactions();
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found");
        }
        return transactions;
    }

    // Delete transaction by ID
    public boolean deleteTransaction(int transactionId) {
        if (transactionId <= 0) {
            throw new IllegalArgumentException("Transaction ID must be greater than zero");
        }
        return transactionPersistenceService.deleteTransaction(transactionId);
    }
}
