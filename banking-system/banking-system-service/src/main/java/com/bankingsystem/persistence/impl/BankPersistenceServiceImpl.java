package com.bankingsystem.persistence.impl;

import com.bankingsystem.exception.BankNotFoundException;
import com.bankingsystem.mapper.BankMapper;
import com.bankingsystem.model.Bank;
import com.bankingsystem.model.BankEntity;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankPersistenceServiceImpl implements BankPersistenceService {

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
        BankEntity entity = bankMapper.toEntity(bank);
        BankEntity savedEntity = bankRepository.save(entity);
        return bankMapper.toModel(savedEntity);
    }

    // Get bank by BIC
    @Override
    public Bank getBankByBic(String bic) {
        BankEntity entity = bankRepository.findByBic(bic).orElseThrow(() -> new BankNotFoundException("Bank not found"));
        return bankMapper.toModel(entity);
    }

    // Delete bank by BIC
    @Override
    public boolean deleteBank(String bic) {
        Optional<BankEntity> bankToDelete = bankRepository.findByBic(bic);
        bankToDelete.ifPresent(bankRepository::delete);
        return bankToDelete.isPresent();
    }
}
