package com.bankingsystem.domain.mapper;

import com.bankingsystem.domain.model.SavingsAccount;
import com.bankingsystem.persistence.model.SavingsAccountEntity;
import com.bankingsystem.persistence.model.UserEntity;
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
        entity.setIban(model.getIban());
        entity.setAccountName(model.getAccountName());
        // Map only the userId to prevent creating a new UserEntity
        if (model.getOwner() != null && model.getOwner().getUserId() != null) {
            UserEntity ownerEntity = new UserEntity();
            ownerEntity.setUserId(model.getOwner().getUserId());
            entity.setOwner(ownerEntity);
        }
        entity.setOwner(userMapper.toEntity(model.getOwner()));
        entity.setBalance(model.getBalance());
        entity.setCurrency(model.getCurrency());
        entity.setInterestRatePercentage(model.getInterestRatePercentage());
        return entity;
    }
}
