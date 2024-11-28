package com.domain.persistence.impl;

import com.bankingsystem.exception.AccountNotFoundException;
import com.domain.mapper.JointCheckingAccountMapper;
import com.domain.mapper.UserMapper;
import com.domain.model.JointCheckingAccount;
import com.bankingsystem.model.JointCheckingAccountEntity;
import com.domain.persistence.JointCheckingAccountPersistenceService;
import com.bankingsystem.repository.JointCheckingAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class JointCheckingAccountPersistenceServiceImpl implements JointCheckingAccountPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(JointCheckingAccountPersistenceServiceImpl.class);

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
        logger.info("Saving new joint checking account");
        JointCheckingAccountEntity entity = jointCheckingAccountMapper.toEntity(account);
        JointCheckingAccountEntity savedEntity = jointCheckingAccountRepository.save(entity);
        logger.info("Successfully saved joint checking account with ID: {}", savedEntity.getAccountId());
        return jointCheckingAccountMapper.toModel(savedEntity);
    }

    @Override
    public JointCheckingAccount getAccountById(int accountId) {
        JointCheckingAccountEntity entity = jointCheckingAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    logger.error("Joint Checking Account not found for ID: {}", accountId);
                    return new AccountNotFoundException("Joint Checking Account not found");
                });
        logger.info("Successfully fetched joint checking account with ID: {}", accountId);
        return jointCheckingAccountMapper.toModel(entity);
    }

    @Override
    public List<JointCheckingAccount> getAllAccounts() {
        List<JointCheckingAccountEntity> entities = jointCheckingAccountRepository.findAll();
        if (entities.isEmpty()) {
            logger.error("No joint checking accounts found");
            throw new AccountNotFoundException("No joint checking accounts found");
        }
        List<JointCheckingAccount> accounts = entities.stream()
                .map(jointCheckingAccountMapper::toModel)
                .collect(Collectors.toList());
        logger.info("Successfully fetched all joint checking accounts");
        return accounts;
    }

    @Override
    public JointCheckingAccount updateAccount(JointCheckingAccount account) {
        JointCheckingAccountEntity existingEntity = jointCheckingAccountRepository.findById(account.getAccountId())
                .orElseThrow(() -> {
                    logger.error("Joint Checking Account not found for ID: {}", account.getAccountId());
                    return new AccountNotFoundException("Joint Checking Account not found");
                });

        // update fields of existing entity
        existingEntity.setAccountId(account.getAccountId());
        existingEntity.setIban(account.getIban());
        existingEntity.setAccountName(account.getAccountName());
        existingEntity.setOwner(userMapper.toEntity(account.getOwner()));
        existingEntity.setSecondOwner(userMapper.toEntity(account.getSecondOwner()));
        existingEntity.setBalance(account.getBalance());
        existingEntity.setCurrency(account.getCurrency());
        existingEntity.setOverdraftLimit(account.getOverdraftLimit());

        JointCheckingAccountEntity updatedEntity = jointCheckingAccountRepository.save(existingEntity);
        logger.info("Successfully updated joint checking account with ID: {}", updatedEntity.getAccountId());
        return jointCheckingAccountMapper.toModel(updatedEntity);
    }

    @Override
    public boolean deleteAccount(int accountId) {
        if (!jointCheckingAccountRepository.existsById(accountId)) {
            logger.error("Joint Checking Account not found for ID: {}", accountId);
            throw new AccountNotFoundException("Joint Checking Account not found");
        }
        jointCheckingAccountRepository.deleteById(accountId);
        return true;
    }
}
