package com.bankingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bankingsystem.model.DepositTransaction;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.model.TransferTransaction;
import com.bankingsystem.model.WithdrawTransaction;
import com.bankingsystem.exception.TransactionNotFoundException;
import com.bankingsystem.persistence.TransactionPersistenceService;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {

    private final TransactionPersistenceService transactionPersistenceService;

    public TransactionService(TransactionPersistenceService transactionPersistenceService) {
        this.transactionPersistenceService = transactionPersistenceService;
    }

    // Create new deposit transaction
    public DepositTransaction createDepositTransaction(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        DepositTransaction transaction = new DepositTransaction(amount);
        transactionPersistenceService.save(transaction);
        return transaction;
    }

    // Create new withdraw transaction
    public WithdrawTransaction createWithdrawTransaction(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        WithdrawTransaction transaction = new WithdrawTransaction(amount);
        transactionPersistenceService.save(transaction);
        return transaction;
    }

    // Create new transfer transaction
    public TransferTransaction createTransferTransaction(BigDecimal amount, String fromAccountId, String toAccountId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        TransferTransaction transaction = new TransferTransaction(amount, fromAccountId, toAccountId);
        transactionPersistenceService.save(transaction);
        return transaction;
    }

    // Get transaction by ID
    public Transaction getTransactionById(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        Transaction transaction = transactionPersistenceService.getTransactionById(transactionId);
        if (transaction == null) {
            throw new TransactionNotFoundException("Transaction not found");
        }
        return transaction;
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
    public boolean deleteTransaction(String transactionId) {
        return transactionPersistenceService.deleteTransaction(transactionId);
    }
}
