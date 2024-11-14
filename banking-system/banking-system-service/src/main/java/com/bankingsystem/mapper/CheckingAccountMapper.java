package com.bankingsystem.mapper;

import com.bankingsystem.model.CheckingAccount;
import com.bankingsystem.model.CheckingAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class CheckingAccountMapper {

    private final UserMapper userMapper;

    public CheckingAccountMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CheckingAccount toModel (CheckingAccountEntity entity) {
        if (entity == null) return null;
        CheckingAccount model = new CheckingAccount();
        model.setAccountId(entity.getAccountId());
        model.setIban(entity.getIban());
        model.setAccountName(entity.getAccountName());
        model.setOwner(userMapper.toModel(entity.getOwner()));
        model.setBalance(entity.getBalance());
        model.setCurrency(entity.getCurrency());
        model.setOverdraftLimit(entity.getOverdraftLimit());
        return model;
    }

    public CheckingAccountEntity toEntity (CheckingAccount model) {
        if (model == null) return null;
        CheckingAccountEntity entity = new CheckingAccountEntity();
        entity.setAccountId(model.getAccountId());
        entity.setIban(model.getIban());
        entity.setAccountName(model.getAccountName());
        entity.setOwner(userMapper.toEntity(model.getOwner()));
        entity.setBalance(model.getBalance());
        entity.setCurrency(model.getCurrency());
        entity.setOverdraftLimit(model.getOverdraftLimit());
        return entity;
    }
}
