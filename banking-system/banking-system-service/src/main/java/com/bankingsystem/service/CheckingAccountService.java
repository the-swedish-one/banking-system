package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.model.CheckingAccount;
import com.bankingsystem.model.DepositTransaction;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.persistence.CheckingAccountPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CheckingAccountService {

    private final CheckingAccountPersistenceService checkingAccountPersistenceService;

    public CheckingAccountService(CheckingAccountPersistenceService checkingAccountPersistenceService) {
        this.checkingAccountPersistenceService = checkingAccountPersistenceService;
    }

//    TODO: Implement the following methods
    // Deposit
    public void deposit(int accountId, BigDecimal amount) {
        CheckingAccount checkingAccount = checkingAccountPersistenceService.getAccountById(accountId);
        checkingAccount.deposit(amount);
        checkingAccountPersistenceService.updateAccount(checkingAccount);
        Transaction transaction = transactionService.createTransaction(amount);
    }

//    TODO
    // Withdraw

//    TODO
    // Transfer


    // Create new checking account
    public CheckingAccount createCheckingAccount(CheckingAccount checkingAccount) {
        return checkingAccountPersistenceService.save(checkingAccount);
    }

    // Get checking account by ID
    public CheckingAccount getCheckingAccountById(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return checkingAccountPersistenceService.getAccountById(accountId);
    }

    // Get all checking accounts
    public List<CheckingAccount> getAllCheckingAccounts() {
        List<CheckingAccount> checkingAccountList = checkingAccountPersistenceService.getAllAccounts();
        if (checkingAccountList.isEmpty()) {
            throw new AccountNotFoundException("No checking accounts found");
        }
        return checkingAccountList;
    }

    // Update checking account
    public CheckingAccount updateCheckingAccount(CheckingAccount checkingAccount) {
        return checkingAccountPersistenceService.updateAccount(checkingAccount);
    }

    // Delete checking account by ID
    public boolean deleteCheckingAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return checkingAccountPersistenceService.deleteAccount(accountId);
    }
}
