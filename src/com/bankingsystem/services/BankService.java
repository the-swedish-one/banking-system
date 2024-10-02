package com.bankingsystem.services;

import com.bankingsystem.models.Bank;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.persistence.dao.BankDAO;

public class BankService {

    private final BankPersistenceService bankPersistenceService;

    public BankService(BankPersistenceService bankPersistenceService) {
        this.bankPersistenceService = bankPersistenceService;
    }

    public void createBank(String bic) {
        Bank bank = new Bank(bic);
        bankPersistenceService.createBank(bank);
    }
}
