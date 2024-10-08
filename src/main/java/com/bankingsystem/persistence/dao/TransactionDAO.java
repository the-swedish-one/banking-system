package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Transaction;
import com.bankingsystem.persistence.TransactionPersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionDAO implements TransactionPersistenceService {

    private List<Transaction> transactions = new ArrayList<>();

    // Create a new transaction
    @Override
    public void createTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    // Get one transaction by ID
    @Override
    public Transaction getTransactionById(String transactionId) {
        for (Transaction transaction : transactions) {
            if (Objects.equals(transaction.getTransactionId(), transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    // Get all transactions
    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // Update transaction
    @Override
    public void updateTransaction(Transaction transaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (Objects.equals(transactions.get(i).getTransactionId(), transaction.getTransactionId())) {
                transactions.set(i, transaction);
                return;
            }
        }
    }

    // Delete an transaction
    @Override
    public boolean deleteTransaction(String transactionId) {
        return transactions.removeIf(account -> Objects.equals(account.getTransactionId(), transactionId));
    }
}
