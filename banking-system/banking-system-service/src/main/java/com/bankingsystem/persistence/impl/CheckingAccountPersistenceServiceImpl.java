package com.bankingsystem.persistence.impl;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.mapper.CheckingAccountMapper;
import com.bankingsystem.mapper.UserMapper;
import com.bankingsystem.model.CheckingAccount;
import com.bankingsystem.model.CheckingAccountEntity;
import com.bankingsystem.persistence.CheckingAccountPersistenceService;
import com.bankingsystem.repository.CheckingAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CheckingAccountPersistenceServiceImpl implements CheckingAccountPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(UserPersistenceServiceImpl.class);

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
        logger.info("Saving new checking account");
        CheckingAccountEntity entity = checkingAccountMapper.toEntity(account);
        CheckingAccountEntity savedEntity = checkingAccountRepository.save(entity);
        logger.info("Successfully saved checking account with ID: {}", savedEntity.getAccountId());
        return checkingAccountMapper.toModel(savedEntity);
    }

    @Override
    public CheckingAccount getAccountById(int accountId) {
        CheckingAccountEntity entity = checkingAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    logger.error("Checking Account not found for ID: {}", accountId);
                    return new AccountNotFoundException("Checking Account not found");
                });
        return checkingAccountMapper.toModel(entity);
    }

    @Override
    public List<CheckingAccount> getAllAccounts() {
        List<CheckingAccountEntity> entities = checkingAccountRepository.findAll();
        if (entities.isEmpty()) {
            logger.error("No checking accounts found");
            throw new AccountNotFoundException("No checking accounts found");
        }
        List<CheckingAccount> accounts = entities.stream()
                .map(checkingAccountMapper::toModel)
                .collect(Collectors.toList());
        logger.info("Successfully fetched all checking accounts");
        return accounts;
    }

    @Override
    public CheckingAccount updateAccount(CheckingAccount account) {
        CheckingAccountEntity existingEntity = checkingAccountRepository.findById(account.getAccountId())
                .orElseThrow(() -> {
                    logger.error("Checking Account not found for ID: {}", account.getAccountId());
                    return new AccountNotFoundException("Checking Account not found");
                });

        // update fields of existing entity
        existingEntity.setAccountId(account.getAccountId());
        existingEntity.setIban(account.getIban());
        existingEntity.setAccountName(account.getAccountName());
        existingEntity.setOwner(userMapper.toEntity(account.getOwner()));
        existingEntity.setBalance(account.getBalance());
        existingEntity.setCurrency(account.getCurrency());
        existingEntity.setOverdraftLimit(account.getOverdraftLimit());

        CheckingAccountEntity updatedEntity = checkingAccountRepository.save(existingEntity);
        logger.info("Successfully updated checking account with ID: {}", updatedEntity.getAccountId());
        return checkingAccountMapper.toModel(updatedEntity);
    }

    @Override
    public boolean deleteAccount(int accountId) {
        if (!checkingAccountRepository.existsById(accountId)) {
            logger.error("Checking Account not found for ID: {}", accountId);
            throw new AccountNotFoundException("Savings Account not found");
        }
        checkingAccountRepository.deleteById(accountId);
        return true;
    }
}
