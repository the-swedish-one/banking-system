package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Bank;
import com.bankingsystem.persistence.BankPersistenceService;

import java.util.ArrayList;
import java.util.List;

public class BankDAO implements BankPersistenceService {

    private List<Bank> banks = new ArrayList<>();

    // Create new bank
    @Override
    public void createBank(Bank bank){
        banks.add(bank);
    }

    // Get bank by BIC
    public Bank getBankByBic(String bic){
        for (Bank bank : banks) {
            if (bank.getBic().equals(bic)) {
                return bank;
            }
        }
        return null;
    }

    // Update bank
    public void updateBank(Bank bank){
        for (int i = 0; i < banks.size(); i++) {
            if (banks.get(i).getBic().equals(bank.getBic())) {
                banks.set(i, bank);
                return;
            }
        }
    }

    //Delete bank by BIC
    public boolean deleteBank(String bic){
        for (int i = 0; i < banks.size(); i++) {
            if (banks.get(i).getBic().equals(bic)) {
                banks.remove(i);
                return true;
            }
        }
        return false;
    }

}
