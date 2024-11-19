package com.bankingsystem.persistence.impl;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.mapper.SavingsAccountMapper;
import com.bankingsystem.mapper.UserMapper;
import com.bankingsystem.model.SavingsAccount;
import com.bankingsystem.model.SavingsAccountEntity;
import com.bankingsystem.persistence.SavingsAccountPersistenceService;
import com.bankingsystem.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingsAccountPersistenceServiceImpl implements SavingsAccountPersistenceService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final SavingsAccountMapper savingsAccountMapper;
    private final UserMapper userMapper;

    @Autowired
    public SavingsAccountPersistenceServiceImpl(SavingsAccountRepository savingsAccountRepository, SavingsAccountMapper savingsAccountMapper, UserMapper userMapper) {
        this.savingsAccountRepository = savingsAccountRepository;
        this.savingsAccountMapper = savingsAccountMapper;
        this.userMapper = userMapper;
    }

    // Create or save a new savings account
    @Override
    public SavingsAccount save(SavingsAccount account) {
        SavingsAccountEntity entity = savingsAccountMapper.toEntity(account);
        SavingsAccountEntity savedEntity = savingsAccountRepository.save(entity);
        return savingsAccountMapper.toModel(savedEntity);
    }

    @Override
    public SavingsAccount getAccountById(int accountId) {
        SavingsAccountEntity entity = savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Savings Account not found"));
        return savingsAccountMapper.toModel(entity);
    }

    @Override
    public List<SavingsAccount> getAllAccounts() {
        return savingsAccountRepository.findAll().stream()
                .map(savingsAccountMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public SavingsAccount updateAccount(SavingsAccount account) {
        SavingsAccountEntity existingEntity = savingsAccountRepository.findById(account.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Savings Account not found"));

        // update fields of existing entity
        existingEntity.setAccountId(account.getAccountId());
        existingEntity.setIban(account.getIban());
        existingEntity.setAccountName(account.getAccountName());
        existingEntity.setOwner(userMapper.toEntity(account.getOwner()));
        existingEntity.setBalance(account.getBalance());
        existingEntity.setCurrency(account.getCurrency());
        existingEntity.setInterestRatePercentage(account.getInterestRatePercentage());

        SavingsAccountEntity updatedEntity = savingsAccountRepository.save(existingEntity);
        return savingsAccountMapper.toModel(updatedEntity);
    }

    @Override
    public boolean deleteAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        if (!savingsAccountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Savings Account not found");
        }
        savingsAccountRepository.deleteById(accountId);
        return true;
    }
}
