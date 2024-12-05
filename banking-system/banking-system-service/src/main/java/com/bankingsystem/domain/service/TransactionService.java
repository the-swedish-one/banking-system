package com.bankingsystem.domain.service;

import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.persistence.exception.TransactionNotFoundException;
import com.bankingsystem.domain.persistence.TransactionPersistenceService;
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
        if (transaction == null) {
            logger.error("Transaction object is null");
            throw new IllegalArgumentException("Transaction object cannot be null");
        }
        if (transaction.getFromAccountIban() == null && transaction.getToAccountIban() == null) {
            logger.error("At least one of fromAccountIban or toAccountIban must be specified");
            throw new IllegalArgumentException("At least one of fromAccountIban or toAccountIban must be specified");
        }
        if (transaction.getAmount() == null) {
            logger.error("Transaction amount is null");
            throw new IllegalArgumentException("Transaction amount cannot be null");
        }
        if(transaction.getAmount().equals(BigDecimal.ZERO)) {
            logger.error("Transaction amount cannot be zero");
            throw new IllegalArgumentException("Transaction amount cannot be zero");
        }
        logger.info("Creating new transaction");
        return transactionPersistenceService.save(transaction);
    }

    // Get transaction by ID
    public Transaction getTransactionById(Integer transactionId) {
        if(transactionId == null) {
            logger.error("Transaction ID is null");
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }
        logger.info("Fetching transaction by ID: {}", transactionId);
        return transactionPersistenceService.getTransactionById(transactionId);
    }


    // Get all transactions
    public List<Transaction> getAllTransactions() {
        logger.info("Fetching all transactions");
        return transactionPersistenceService.getAllTransactions();
    }

    // Get all transactions for an IBAN
    public List<Transaction> getTransactionsByIban(String iban) {
        if(iban == null) {
            logger.error("IBAN is null");
            throw new IllegalArgumentException("IBAN cannot be null");
        }
        logger.info("Fetching all transactions for IBAN: {}", iban);
        return transactionPersistenceService.getTransactionsByIban(iban);
    }

    // Delete transaction by ID
    public boolean deleteTransaction(Integer transactionId) {
        logger.info("Deleting transaction by ID: {}", transactionId);
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
