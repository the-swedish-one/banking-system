package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.model.SavingsAccount;
import com.bankingsystem.persistence.SavingsAccountPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountService {

    private final SavingsAccountPersistenceService savingsAccountPersistenceService;

    public SavingsAccountService(SavingsAccountPersistenceService savingsAccountPersistenceService) {
        this.savingsAccountPersistenceService = savingsAccountPersistenceService;
    }

    // Create new savings account
    public SavingsAccount createSavingsAccount(SavingsAccount savingsAccount) {
        return savingsAccountPersistenceService.save(savingsAccount);
    }

    // Get savings account by ID
    public SavingsAccount getSavingsAccountById(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return savingsAccountPersistenceService.getAccountById(accountId);
    }

    // Get all savings accounts
    public List<SavingsAccount> getAllSavingsAccounts() {
        List<SavingsAccount> savingsAccountList = savingsAccountPersistenceService.getAllAccounts();
        if (savingsAccountList.isEmpty()) {
            throw new AccountNotFoundException("No savings accounts found");
        }
        return savingsAccountList;
    }

    // Update savings account
    public SavingsAccount updateSavingsAccount(SavingsAccount savingsAccount) {
        return savingsAccountPersistenceService.updateAccount(savingsAccount);
    }

    // Delete savings account by ID
    public boolean deleteSavingsAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return savingsAccountPersistenceService.deleteAccount(accountId);
    }
}
