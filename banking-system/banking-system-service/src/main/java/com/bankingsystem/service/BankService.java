package com.bankingsystem.service;

import com.bankingsystem.exception.BankNotFoundException;
import org.springframework.stereotype.Service;

import com.bankingsystem.model.Bank;
import com.bankingsystem.persistence.BankPersistenceService;


@Service
public class BankService {

    private final BankPersistenceService bankPersistenceService;

    public BankService(BankPersistenceService bankPersistenceService) {
        this.bankPersistenceService = bankPersistenceService;
    }

    // Create and save a new bank
    public Bank createBank(String bankName, String bic) {
        Bank bank = new Bank(bankName, bic);
        return bankPersistenceService.save(bank);
    }

    // Get bank by BIC
    public Bank getBankByBic(String bic) {
        Bank bank = bankPersistenceService.getBankByBic(bic);
        if (bank == null) {
            throw new BankNotFoundException("Bank not found");
        }
        return bank;
    }

    // Update bank
    public Bank updateBank(String bic, String newBankName) {
        Bank bank = bankPersistenceService.getBankByBic(bic);
        if (bank == null) {
            throw new BankNotFoundException("Bank not found");
        }
        bank.setBankName(newBankName);
        bankPersistenceService.save(bank);
        return bank;
    }

}
