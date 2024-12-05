package com.bankingsystem.domain.mapper;

import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.persistence.model.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toModel(TransactionEntity entity) {
        if (entity == null) return null;

        return new Transaction(
                entity.getTransactionId(),
                entity.getAmount(),
                entity.getTimestamp(),
                entity.getFromAccountIban(),
                entity.getToAccountIban()
        );
    }

    public TransactionEntity toEntity(Transaction transaction) {
        if (transaction == null) return null;

        return TransactionEntity.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .fromAccountIban(transaction.getFromAccountIban())
                .toAccountIban(transaction.getToAccountIban())
                .build();
    }
}
