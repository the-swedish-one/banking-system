package com.bankingsystem.mapper;

import com.bankingsystem.model.Transaction;
import com.bankingsystem.model.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toModel(TransactionEntity entity) {
        if (entity == null) return null;
       Transaction model = new Transaction();
        model.setTransactionId(entity.getTransactionId());
        model.setAmount(entity.getAmount());
        model.setTimestamp(entity.getTimestamp());
        model.setToAccountId(entity.getToAccountId());
        model.setFromAccountId(entity.getFromAccountId());
        return model;
    }

    public static TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.getAmount(),
                transaction.getFromAccountId(),
                transaction.getToAccountId()
        );
    }
}
