package com.bankingsystem.persistence.impl;

import com.bankingsystem.exception.TransactionNotFoundException;
import com.bankingsystem.model.*;
import com.bankingsystem.persistence.TransactionPersistenceService;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.mapper.TransactionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionPersistenceServiceImpl implements TransactionPersistenceService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionPersistenceServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    // Create or save Transaction
    @Override
    public Transaction save(Transaction depositTransaction) {
        TransactionEntity entity = transactionMapper.toEntity(depositTransaction);
        TransactionEntity savedEntity = transactionRepository.save(entity);
        return transactionMapper.toModel(savedEntity);
    }


    // Get transaction by ID
    @Override
    public Transaction getTransactionById(int transactionId) {
        TransactionEntity entity = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        return transactionMapper.toModel(entity);
    }

    // Get all transactions
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toModel)
                .collect(Collectors.toList());
    }

    // Update transaction
    public Transaction updateTransaction(Transaction transaction) {
        TransactionEntity existingEntity = transactionRepository.findById(transaction.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        // Update fields of the existing transaction
        existingEntity.setTransactionId(transaction.getTransactionId());
        existingEntity.setAmount(transaction.getAmount());
        existingEntity.setFromAccountId(transaction.getFromAccountId());
        existingEntity.setToAccountId(transaction.getToAccountId());

        TransactionEntity updatedEntity = transactionRepository.save(existingEntity);
        return transactionMapper.toModel(updatedEntity);
    }

    // Delete transaction by ID
    @Override
    public boolean deleteTransaction(int transactionId) {
        if(!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException("Transaction not found");
        }

        if (transactionId <= 0) {
            throw new IllegalArgumentException("Transaction ID must be greater than zero");
        }
        transactionRepository.deleteById(transactionId);
        return true;
    }
}
