package com.bankingsystem.persistence.impl;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.mapper.CheckingAccountMapper;
import com.bankingsystem.mapper.UserMapper;
import com.bankingsystem.model.CheckingAccount;
import com.bankingsystem.model.CheckingAccountEntity;
import com.bankingsystem.persistence.CheckingAccountPersistenceService;
import com.bankingsystem.repository.CheckingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CheckingAccountPersistenceServiceImpl implements CheckingAccountPersistenceService {

    private final CheckingAccountRepository checkingAccountRepository;
    private final CheckingAccountMapper checkingAccountMapper;
    private final UserMapper userMapper;

    @Autowired
    public CheckingAccountPersistenceServiceImpl(CheckingAccountRepository checkingAccountRepository, CheckingAccountMapper checkingAccountMapper, UserMapper userMapper) {
        this.checkingAccountRepository = checkingAccountRepository;
        this.checkingAccountMapper = checkingAccountMapper;
        this.userMapper = userMapper;
    }

    // Create or save a new savings account
    @Override
    public CheckingAccount save(CheckingAccount account) {
        CheckingAccountEntity entity = checkingAccountMapper.toEntity(account);
        CheckingAccountEntity savedEntity = checkingAccountRepository.save(entity);
        return checkingAccountMapper.toModel(savedEntity);
    }

    @Override
    public CheckingAccount getAccountById(int accountId) {
        CheckingAccountEntity entity = checkingAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Checking Account not found"));
        return checkingAccountMapper.toModel(entity);
    }

    @Override
    public List<CheckingAccount> getAllAccounts() {
        return checkingAccountRepository.findAll().stream()
                .map(checkingAccountMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public CheckingAccount updateAccount(CheckingAccount account) {
        CheckingAccountEntity existingEntity = checkingAccountRepository.findById(account.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Checking Account not found"));

        // update fields of existing entity
        existingEntity.setAccountId(account.getAccountId());
        existingEntity.setIban(account.getIban());
        existingEntity.setAccountName(account.getAccountName());
        existingEntity.setOwner(userMapper.toEntity(account.getOwner()));
        existingEntity.setBalance(account.getBalance());
        existingEntity.setCurrency(account.getCurrency());
        existingEntity.setOverdraftLimit(account.getOverdraftLimit());

        return checkingAccountMapper.toModel(checkingAccountRepository.save(existingEntity));
    }

    @Override
    public boolean deleteAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        if (!checkingAccountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Savings Account not found");
        }
        checkingAccountRepository.deleteById(accountId);
        return true;
    }
}
