package com.domain.service;

import com.bankingsystem.exception.BankNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.domain.model.Bank;
import com.domain.persistence.BankPersistenceService;


@Service
public class BankService {

    private static final Logger logger = LoggerFactory.getLogger(JointCheckingAccountService.class);

    private final BankPersistenceService bankPersistenceService;

    public BankService(BankPersistenceService bankPersistenceService) {
        this.bankPersistenceService = bankPersistenceService;
    }

    // Create and save a new bank
    public Bank createBank(String bankName, String bic) {
        logger.info("Creating new bank");
        if (bankName == null || bic == null) {
            logger.error("Bank name and BIC cannot be null");
            throw new IllegalArgumentException("Bank name and BIC cannot be null");
        }
        Bank bank = new Bank(bankName, bic);
        logger.info("Successfully created new bank with BIC: {}", bic);
        return bankPersistenceService.save(bank);
    }

    // Get bank by BIC
    public Bank getBankByBic(String bic) {
        logger.info("Fetching bank by BIC: {}", bic);
        Bank bank = bankPersistenceService.getBankByBic(bic);
        if (bank == null) {
            logger.error("Bank not found");
            throw new BankNotFoundException("Bank not found");
        }
        return bank;
    }

    // Update bank
    public Bank updateBank(String bic, String newBankName) {
        logger.info("Updating bank with BIC: {}", bic);
        Bank bank = bankPersistenceService.getBankByBic(bic);
        bank.setBankName(newBankName);
        bankPersistenceService.save(bank);
        return bank;
    }

}
