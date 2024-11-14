package com.bankingsystem.mapper;

import com.bankingsystem.model.Bank;
import com.bankingsystem.model.BankEntity;
import org.springframework.stereotype.Component;

@Component
public class BankMapper {

    public Bank toModel(BankEntity entity) {
        if (entity == null) return null;
        Bank bank = new Bank();
        bank.setBankName(entity.getBankName());
        bank.setBic(entity.getBic());
        return bank;
    }

    public BankEntity toEntity(Bank model) {
        if (model == null) return null;
        BankEntity entity = new BankEntity();
        entity.setBankName(model.getBankName());
        entity.setBic(model.getBic());
        return entity;
    }
}
