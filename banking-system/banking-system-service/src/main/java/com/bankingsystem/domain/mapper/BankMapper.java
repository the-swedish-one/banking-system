package com.bankingsystem.domain.mapper;

import com.bankingsystem.domain.model.Bank;
import com.bankingsystem.persistence.model.BankEntity;
import org.springframework.stereotype.Component;

@Component
public class BankMapper {

    public Bank toModel(BankEntity entity) {
        if (entity == null) return null;
        Bank bank = new Bank();
        bank.setId(entity.getId());
        bank.setBankName(entity.getBankName());
        bank.setBic(entity.getBic());
        bank.setCollectedInterest(entity.getCollectedInterest());
        return bank;
    }

    public BankEntity toEntity(Bank model) {
        if (model == null) return null;
        BankEntity entity = new BankEntity();
        if (model.getId() != null) entity.setId(model.getId());
        entity.setBankName(model.getBankName());
        entity.setBic(model.getBic());
        entity.setCollectedInterest(model.getCollectedInterest());
        return entity;
    }
}
