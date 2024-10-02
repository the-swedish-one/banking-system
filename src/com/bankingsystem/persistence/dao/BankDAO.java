package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Bank;
import com.bankingsystem.persistence.BankPersistenceService;

import java.util.ArrayList;
import java.util.List;

public class BankDAO implements BankPersistenceService {

    private List<Bank> banks = new ArrayList<Bank>();

    // Create new bank
    @Override
    public void createBank(Bank bank){
        banks.add(bank);
    }

}
