package com.domain.mapper;

import com.domain.model.SavingsAccount;
import com.bankingsystem.model.SavingsAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class SavingsAccountMapper {

    private final UserMapper userMapper;

    public SavingsAccountMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SavingsAccount toModel (SavingsAccountEntity entity) {
        if (entity == null) {return null;}
        SavingsAccount model = new SavingsAccount();
        model.setAccountId(entity.getAccountId());
        model.setIban(entity.getIban());
        model.setAccountName(entity.getAccountName());
        model.setOwner(userMapper.toModel(entity.getOwner()));
        model.setBalance(entity.getBalance());
        model.setCurrency(entity.getCurrency());
        model.setInterestRatePercentage(entity.getInterestRatePercentage());
        return model;
    }

    public SavingsAccountEntity toEntity (SavingsAccount model) {
        if (model == null) {return null;}
        SavingsAccountEntity entity = new SavingsAccountEntity();
        entity.setAccountName(model.getAccountName());
        entity.setOwner(userMapper.toEntity(model.getOwner()));
        entity.setBalance(model.getBalance());
        entity.setCurrency(model.getCurrency());
        entity.setInterestRatePercentage(model.getInterestRatePercentage());
        return entity;
    }
}
