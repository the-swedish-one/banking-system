package com.domain.service;

import com.domain.model.Transaction;
import com.bankingsystem.exception.TransactionNotFoundException;
import com.domain.persistence.TransactionPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionPersistenceService transactionPersistenceService;

    public TransactionService(TransactionPersistenceService transactionPersistenceService) {
        this.transactionPersistenceService = transactionPersistenceService;
    }

    // Create new transaction
    public Transaction createTransaction(Transaction transaction) {
        logger.info("Creating new transaction");
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            logger.error("Invalid transaction amount: {}", transaction.getAmount());
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
        if (transaction.getFromAccountId() == null && transaction.getToAccountId() == null) {
            logger.error("At least one of fromAccountId or toAccountId must be specified");
            throw new IllegalArgumentException("At least one of fromAccountId or toAccountId must be specified");
        }
        // check transactions amount is not null
        if (transaction.getAmount() == null) {
            logger.error("Transaction amount is null");
            throw new IllegalArgumentException("Transaction amount cannot be null");
        }
        return transactionPersistenceService.save(transaction);
    }

    // Get transaction by ID
    public Transaction getTransactionById(int transactionId) {
        logger.info("Fetching transaction by ID: {}", transactionId);
        if (transactionId <= 0) {
            logger.error("Invalid transaction ID: {}", transactionId);
            throw new IllegalArgumentException("Transaction ID must be greater than zero");
        }
        return transactionPersistenceService.getTransactionById(transactionId);
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        logger.info("Fetching all transactions");
        return transactionPersistenceService.getAllTransactions();
    }

    // Delete transaction by ID
    public boolean deleteTransaction(int transactionId) {
        logger.info("Deleting transaction by ID: {}", transactionId);
        if (transactionId <= 0) {
            logger.error("Invalid transaction ID: {}", transactionId);
            throw new IllegalArgumentException("Transaction ID must be greater than zero");
        }

        try {
            boolean isDeleted = transactionPersistenceService.deleteTransaction(transactionId);
            logger.info("Successfully deleted transaction for ID: {}", transactionId);
            return isDeleted;
        } catch (TransactionNotFoundException ex) {
            logger.error("Transaction not found for ID: {}", transactionId);
            throw ex;
        }
    }

}
