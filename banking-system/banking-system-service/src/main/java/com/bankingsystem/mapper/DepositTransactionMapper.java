package com.bankingsystem.mapper;

import com.bankingsystem.model.DepositTransaction;
import com.bankingsystem.model.DepositTransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class DepositTransactionMapper {

    public DepositTransaction toModel(DepositTransactionEntity entity) {
        if (entity == null) return null;
        DepositTransaction model = new DepositTransaction();
        model.setTransactionId(entity.getTransactionId());
        model.setAmount(entity.getAmount());
        model.setTimestamp(entity.getTimestamp());
        model.setToAccountId(entity.getToAccountId());
        return model;
    }

    public DepositTransactionEntity toEntity(DepositTransaction model) {
        if (model == null) return null;
        DepositTransactionEntity entity = new DepositTransactionEntity();
        entity.setTransactionId(model.getTransactionId());
        entity.setAmount(model.getAmount());
        entity.setTimestamp(model.getTimestamp());
        entity.setToAccountId(model.getToAccountId());
        return entity;
    }
}
