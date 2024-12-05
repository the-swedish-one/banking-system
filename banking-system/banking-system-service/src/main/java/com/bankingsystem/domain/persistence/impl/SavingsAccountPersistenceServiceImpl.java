package com.bankingsystem.domain.persistence.impl;

import com.bankingsystem.domain.model.JointCheckingAccount;
import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.mapper.SavingsAccountMapper;
import com.bankingsystem.domain.mapper.UserMapper;
import com.bankingsystem.domain.model.SavingsAccount;
import com.bankingsystem.persistence.model.JointCheckingAccountEntity;
import com.bankingsystem.persistence.model.SavingsAccountEntity;
import com.bankingsystem.domain.persistence.SavingsAccountPersistenceService;
import com.bankingsystem.persistence.repository.SavingsAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingsAccountPersistenceServiceImpl implements SavingsAccountPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccountPersistenceServiceImpl.class);

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
        logger.info("Saving new savings account");
        SavingsAccountEntity entity = savingsAccountMapper.toEntity(account);
        SavingsAccountEntity savedEntity = savingsAccountRepository.save(entity);
        logger.info("Successfully saved savings account with ID: {}", savedEntity.getAccountId());
        return savingsAccountMapper.toModel(savedEntity);
    }

    @Override
    public SavingsAccount getAccountById(int accountId) {
        SavingsAccountEntity entity = savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    logger.error("Savings Account not found for ID: {}", accountId);
                    return new AccountNotFoundException("Savings Account not found");
                });
        logger.info("Successfully fetched savings account with ID: {}", accountId);
        return savingsAccountMapper.toModel(entity);
    }

    @Override
    public SavingsAccount getAccountByIban(String iban) {
        SavingsAccountEntity entity = savingsAccountRepository.findByIban(iban)
                .orElseThrow(() -> {
                    logger.error("Savings Account not found for IBAN: {}", iban);
                    return new AccountNotFoundException("Savings Account not found");
                });
        return savingsAccountMapper.toModel(entity);
    }

    @Override
    public List<SavingsAccount> getAllAccounts() {
        List<SavingsAccountEntity> entities = savingsAccountRepository.findAll();
        if (entities.isEmpty()) {
            logger.error("No savings accounts found");
            throw new AccountNotFoundException("No savings accounts found");
        }
        List<SavingsAccount> accounts = entities.stream()
                .map(savingsAccountMapper::toModel)
                .collect(Collectors.toList());
        logger.info("Successfully fetched {} savings accounts", accounts.size());
        return accounts;
    }

    @Override
    public SavingsAccount updateAccount(SavingsAccount account) {
        SavingsAccountEntity existingEntity = savingsAccountRepository.findById(account.getAccountId())
                .orElseThrow(() -> {
                    logger.error("Savings Account not found for ID: {}", account.getAccountId());
                    return new AccountNotFoundException("Savings Account not found");
                });

        // update fields of existing entity
        existingEntity.setAccountId(account.getAccountId());
        existingEntity.setIban(account.getIban());
        existingEntity.setAccountName(account.getAccountName());
        existingEntity.setOwner(userMapper.toEntity(account.getOwner()));
        existingEntity.setBalance(account.getBalance());
        existingEntity.setCurrency(account.getCurrency());
        existingEntity.setInterestRatePercentage(account.getInterestRatePercentage());

        SavingsAccountEntity updatedEntity = savingsAccountRepository.save(existingEntity);
        logger.info("Successfully updated savings account with ID: {}", updatedEntity.getAccountId());
        return savingsAccountMapper.toModel(updatedEntity);
    }

    @Override
    public boolean deleteAccount(int accountId) {
        if (!savingsAccountRepository.existsById(accountId)) {
            logger.error("Savings Account not found for ID: {}", accountId);
            throw new AccountNotFoundException("Savings Account not found");
        }
        savingsAccountRepository.deleteById(accountId);
        return true;
    }
}
