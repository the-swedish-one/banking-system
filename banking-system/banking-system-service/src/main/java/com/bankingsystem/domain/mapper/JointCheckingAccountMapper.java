package com.bankingsystem.domain.mapper;

import com.bankingsystem.domain.model.JointCheckingAccount;
import com.bankingsystem.persistence.model.JointCheckingAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class JointCheckingAccountMapper {

    private final UserMapper userMapper;

    public JointCheckingAccountMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public JointCheckingAccount toModel (JointCheckingAccountEntity entity) {
        JointCheckingAccount model = new JointCheckingAccount();
        model.setAccountId(entity.getAccountId());
        model.setIban(entity.getIban());
        model.setAccountName(entity.getAccountName());
        model.setOwner(userMapper.toModel(entity.getOwner()));
        model.setBalance(entity.getBalance());
        model.setCurrency(entity.getCurrency());
        model.setOverdraftLimit(entity.getOverdraftLimit());
        model.setSecondOwner(userMapper.toModel(entity.getSecondOwner()));
        return model;
    }

    public JointCheckingAccountEntity toEntity (JointCheckingAccount model) {
        JointCheckingAccountEntity entity = new JointCheckingAccountEntity();
        entity.setAccountName(model.getAccountName());
        entity.setOwner(userMapper.toEntity(model.getOwner()));
        entity.setBalance(model.getBalance());
        entity.setCurrency(model.getCurrency());
        entity.setOverdraftLimit(model.getOverdraftLimit());
        entity.setSecondOwner(userMapper.toEntity(model.getSecondOwner()));
        return entity;
    }
}
