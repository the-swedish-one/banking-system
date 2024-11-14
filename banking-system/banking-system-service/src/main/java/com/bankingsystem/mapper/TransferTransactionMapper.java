package com.bankingsystem.mapper;

import com.bankingsystem.model.TransferTransaction;
import com.bankingsystem.model.TransferTransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransferTransactionMapper {

    public TransferTransaction toModel(TransferTransactionEntity entity) {
        if (entity == null) return null;
        TransferTransaction model = new TransferTransaction();
        model.setTransactionId(entity.getTransactionId());
        model.setAmount(entity.getAmount());
        model.setTimestamp(entity.getTimestamp());
        model.setToAccountId(entity.getToAccountId());
        model.setFromAccountId(entity.getFromAccountId());
        return model;
    }

    public TransferTransactionEntity toEntity(TransferTransaction model) {
        if (model == null) return null;
        TransferTransactionEntity entity = new TransferTransactionEntity();
        entity.setTransactionId(model.getTransactionId());
        entity.setAmount(model.getAmount());
        entity.setTimestamp(model.getTimestamp());
        entity.setToAccountId(model.getToAccountId());
        entity.setFromAccountId(model.getFromAccountId());
        return entity;
    }
}
