package com.bankingsystem.persistence.impl;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.mapper.JointCheckingAccountMapper;
import com.bankingsystem.mapper.UserMapper;
import com.bankingsystem.model.JointCheckingAccount;
import com.bankingsystem.model.JointCheckingAccountEntity;
import com.bankingsystem.persistence.JointCheckingAccountPersistenceService;
import com.bankingsystem.repository.JointCheckingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class JointCheckingAccountPersistenceServiceImpl implements JointCheckingAccountPersistenceService {

    private final JointCheckingAccountRepository jointCheckingAccountRepository;
    private final JointCheckingAccountMapper jointCheckingAccountMapper;
    private final UserMapper userMapper;

    @Autowired
    public JointCheckingAccountPersistenceServiceImpl(JointCheckingAccountRepository jointCheckingAccountRepository, JointCheckingAccountMapper jointCheckingAccountMapper, UserMapper userMapper) {
        this.jointCheckingAccountRepository = jointCheckingAccountRepository;
        this.jointCheckingAccountMapper = jointCheckingAccountMapper;
        this.userMapper = userMapper;
    }

    // Create or save a new savings account
    @Override
    public JointCheckingAccount save(JointCheckingAccount account) {
        JointCheckingAccountEntity entity = jointCheckingAccountMapper.toEntity(account);
        JointCheckingAccountEntity savedEntity = jointCheckingAccountRepository.save(entity);
        return jointCheckingAccountMapper.toModel(savedEntity);
    }

    @Override
    public JointCheckingAccount getAccountById(int accountId) {
        JointCheckingAccountEntity entity = jointCheckingAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Joint Checking Account not found"));
        return jointCheckingAccountMapper.toModel(entity);
    }

    @Override
    public List<JointCheckingAccount> getAllAccounts() {
        return jointCheckingAccountRepository.findAll().stream()
                .map(jointCheckingAccountMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public JointCheckingAccount updateAccount(JointCheckingAccount account) {
        JointCheckingAccountEntity existingEntity = jointCheckingAccountRepository.findById(account.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Joint Checking Account not found"));

        // update fields of existing entity
        existingEntity.setAccountId(account.getAccountId());
        existingEntity.setIban(account.getIban());
        existingEntity.setAccountName(account.getAccountName());
        existingEntity.setOwner(userMapper.toEntity(account.getOwner()));
        existingEntity.setSecondOwner(userMapper.toEntity(account.getSecondOwner()));
        existingEntity.setBalance(account.getBalance());
        existingEntity.setCurrency(account.getCurrency());
        existingEntity.setOverdraftLimit(account.getOverdraftLimit());

        return jointCheckingAccountMapper.toModel(jointCheckingAccountRepository.save(existingEntity));
    }

    @Override
    public boolean deleteAccount(int accountId) {
        if (accountId <=  0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        if (!jointCheckingAccountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Joint Checking Account not found");
        }
        jointCheckingAccountRepository.deleteById(accountId);
        return true;
    }
}
