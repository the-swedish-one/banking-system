package com.bankingsystem.mapper;

import com.bankingsystem.model.WithdrawTransaction;
import com.bankingsystem.model.WithdrawTransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class WithdrawTransactionMapper {

    public WithdrawTransaction toModel(WithdrawTransactionEntity entity) {
        if (entity == null) return null;
        WithdrawTransaction model = new WithdrawTransaction();
        model.setTransactionId(entity.getTransactionId());
        model.setAmount(entity.getAmount());
        model.setTimestamp(entity.getTimestamp());
        model.setFromAccountId(entity.getFromAccountId());
        return model;
    }

    public WithdrawTransactionEntity toEntity(WithdrawTransaction model) {
        if (model == null) return null;
        WithdrawTransactionEntity entity = new WithdrawTransactionEntity();
        entity.setTransactionId(model.getTransactionId());
        entity.setAmount(model.getAmount());
        entity.setTimestamp(model.getTimestamp());
        model.setFromAccountId(entity.getFromAccountId());
        return entity;
    }
}
