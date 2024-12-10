package com.bankingsystem.domain.mapper;

import com.bankingsystem.domain.model.JointCheckingAccount;
import com.bankingsystem.persistence.model.JointCheckingAccountEntity;
import com.bankingsystem.persistence.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class JointCheckingAccountMapper {

    private final UserMapper userMapper;

    public JointCheckingAccountMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public JointCheckingAccount toModel (JointCheckingAccountEntity entity) {
        if (entity == null) return null;
        JointCheckingAccount model = new JointCheckingAccount();
        model.setAccountId(entity.getAccountId());
        model.setIban(entity.getIban());
        model.setAccountName(entity.getAccountName());
        model.setOwner(userMapper.toModel(entity.getOwner()));
        model.setBalance(entity.getBalance());
        model.setCurrency(entity.getCurrency());
        model.setOverdraftLimit(entity.getOverdraftLimit());
        model.setOverdraftTimestamp(entity.getOverdraftTimestamp());
        model.setSecondOwner(userMapper.toModel(entity.getSecondOwner()));
        return model;
    }

    public JointCheckingAccountEntity toEntity (JointCheckingAccount model) {
        if (model == null) return null;
        JointCheckingAccountEntity entity = new JointCheckingAccountEntity();
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
        entity.setOverdraftLimit(model.getOverdraftLimit());
        entity.setOverdraftTimestamp(model.getOverdraftTimestamp());
        if (model.getSecondOwner() != null && model.getSecondOwner().getUserId() != null) {
            UserEntity secondOwnerEntity = new UserEntity();
            secondOwnerEntity.setUserId(model.getSecondOwner().getUserId());
            entity.setSecondOwner(secondOwnerEntity);
        }
        entity.setSecondOwner(userMapper.toEntity(model.getSecondOwner()));
        return entity;
    }
}
