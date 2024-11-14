package com.bankingsystem.persistence.impl;

import com.bankingsystem.model.TransactionEntity;
import com.bankingsystem.persistence.TransactionPersistenceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionPersistenceServiceImpl implements TransactionPersistenceService {

    private List<TransactionEntity> transactions = new ArrayList<>();

    // Create a new transaction
    @Override
    public TransactionEntity save(TransactionEntity transaction) {
        transactions.add(transaction);
        return transaction;
    }

    // Get one transaction by ID
    @Override
    public TransactionEntity getTransactionById(String transactionId) {
        return transactions.stream()
                .filter(transaction -> Objects.equals(transaction.getTransactionId(), transactionId))
                .findFirst()
                .orElse(null);
    }

    // Get all transactions
    @Override
    public List<TransactionEntity> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // Update transaction
    @Override
    public void updateTransaction(TransactionEntity transaction) {
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
