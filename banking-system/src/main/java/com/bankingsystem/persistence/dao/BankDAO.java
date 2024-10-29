package com.bankingsystem.persistence.dao;

import com.bankingsystem.model.Bank;
import com.bankingsystem.persistence.BankPersistenceService;

import java.util.ArrayList;
import java.util.List;

public class BankDAO implements BankPersistenceService {

    private List<Bank> banks = new ArrayList<>();

    // Create new bank
    @Override
    public Bank save(Bank bank){
        banks.add(bank);
        return bank;
    }

    // Get bank by BIC
    public Bank getBankByBic(String bic){
        return banks.stream()
                .filter(bank -> bank.getBankId().equals(bic))
                .findFirst()
                .orElse(null);
    }

    // Update bank
    public void updateBank(Bank bank){
        banks.stream()
                .filter(b -> b.getBankId().equals(bank.getBankId()))
                .findFirst()
                .ifPresent(b -> b = bank);
    }

    //Delete bank by BIC
    public boolean deleteBank(String bic){
        return banks.removeIf(bank -> bank.getBankId().equals(bic));
    }
}
