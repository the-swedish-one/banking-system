package com.bankingsystem.domain.persistence.impl;

import com.bankingsystem.persistence.exception.TransactionNotFoundException;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.persistence.TransactionPersistenceService;
import com.bankingsystem.persistence.model.TransactionEntity;
import com.bankingsystem.persistence.repository.TransactionRepository;
import com.bankingsystem.domain.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionPersistenceServiceImpl implements TransactionPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionPersistenceServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionPersistenceServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    // Create or save Transaction
    @Override
    public Transaction save(Transaction depositTransaction) {
        logger.info("Saving new transaction");
        if (depositTransaction == null) {
            logger.error("Transaction object is null");
            throw new IllegalArgumentException("Transaction object cannot be null");
        }
        TransactionEntity entity = transactionMapper.toEntity(depositTransaction);
        TransactionEntity savedEntity = transactionRepository.save(entity);
        logger.info("Successfully saved transaction with ID: {}", savedEntity.getTransactionId());
        return transactionMapper.toModel(savedEntity);
    }

    // Get transaction by ID
    @Override
    public Transaction getTransactionById(int transactionId) {
        TransactionEntity entity = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    logger.error("Transaction not found for ID: {}", transactionId);
                    return new TransactionNotFoundException("Transaction not found");
                });
        logger.info("Successfully fetched transaction with ID: {}", transactionId);
        return transactionMapper.toModel(entity);
    }

    // Get all transactions
    @Override
    public List<Transaction> getAllTransactions() {
        List<TransactionEntity> entities = transactionRepository.findAll();
        if (entities.isEmpty()) {
            logger.error("No transactions found");
            throw new TransactionNotFoundException("No transactions found");
        }
        List<Transaction> transactions = entities.stream()
                .map(transactionMapper::toModel)
                .collect(Collectors.toList());
        logger.info("Successfully fetched {} transactions", transactions.size());
        return transactions;
    }

    // Update transaction
    public Transaction updateTransaction(Transaction transaction) {
        TransactionEntity existingEntity = transactionRepository.findById(transaction.getTransactionId())
                .orElseThrow(() -> {
                    logger.error("Transaction not found for ID: {}", transaction.getTransactionId());
                    return new TransactionNotFoundException("Transaction not found");
                });

        // Update fields of the existing transaction
        existingEntity.setTransactionId(transaction.getTransactionId());
        existingEntity.setAmount(transaction.getAmount());
        existingEntity.setFromAccountId(transaction.getFromAccountId());
        existingEntity.setToAccountId(transaction.getToAccountId());

        TransactionEntity updatedEntity = transactionRepository.save(existingEntity);
        logger.info("Successfully updated transaction with ID: {}", updatedEntity.getTransactionId());
        return transactionMapper.toModel(updatedEntity);
    }

    // Delete transaction by ID
    @Override
    public boolean deleteTransaction(int transactionId) {
        if(!transactionRepository.existsById(transactionId)) {
            logger.error("Transaction not found for ID: {}", transactionId);
            throw new TransactionNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(transactionId);
        return true;
    }
}
