package com.bankingsystem.domain.persistence.impl;

import com.bankingsystem.persistence.exception.BankNotFoundException;
import com.bankingsystem.domain.mapper.BankMapper;
import com.bankingsystem.domain.model.Bank;
import com.bankingsystem.persistence.model.BankEntity;
import com.bankingsystem.domain.persistence.BankPersistenceService;
import com.bankingsystem.persistence.repository.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankPersistenceServiceImpl implements BankPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(BankPersistenceServiceImpl.class);

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    @Autowired
    public BankPersistenceServiceImpl(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    // Create or update bank
    @Override
    public Bank save(Bank bank){
        logger.info("Saving bank");
        BankEntity entity = bankMapper.toEntity(bank);
        BankEntity savedEntity = bankRepository.save(entity);
        logger.info("Successfully saved bank with BIC: {}", savedEntity.getBic());
        return bankMapper.toModel(savedEntity);
    }

    // Get bank by BIC
    @Override
    public Bank getBankByBic(String bic) {
        BankEntity entity = bankRepository.findByBic(bic).orElseThrow(() -> {
            logger.error("Bank not found");
            return new BankNotFoundException("Bank not found");
        });
        logger.info("Successfully fetched bank with BIC: {}", bic);
        return bankMapper.toModel(entity);
    }
}
