package com.bankingsystem.persistence.dao;

import com.bankingsystem.model.Transaction;
import com.bankingsystem.persistence.TransactionPersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionDAO implements TransactionPersistenceService {

    private List<Transaction> transactions = new ArrayList<>();

    // Create a new transaction
    @Override
    public Transaction save(Transaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    // Get one transaction by ID
    @Override
    public Transaction getTransactionById(String transactionId) {
        return transactions.stream()
                .filter(transaction -> Objects.equals(transaction.getTransactionId(), transactionId))
                .findFirst()
                .orElse(null);
    }

    // Get all transactions
    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // Update transaction
    @Override
    public void updateTransaction(Transaction transaction) {
        transactions.stream()
                .filter(t -> t.getTransactionId().equals(transaction.getTransactionId()))
                .findFirst()
                .ifPresent(t -> t = transaction);
    }

    // Delete an transaction
    @Override
    public boolean deleteTransaction(String transactionId) {
        return transactions.removeIf(account -> Objects.equals(account.getTransactionId(), transactionId));
    }
}
