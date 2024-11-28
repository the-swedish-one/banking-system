package com.domain.persistence;

import com.domain.model.SavingsAccount;

import java.util.List;

public interface SavingsAccountPersistenceService {

    // Create a new account
    SavingsAccount save(SavingsAccount account);

    // Get one account by ID
    SavingsAccount getAccountById(int accountId);

    // Get all accounts
    List<SavingsAccount> getAllAccounts();

    // Update account
    SavingsAccount updateAccount(SavingsAccount account);

    // Delete an account
    boolean deleteAccount(int accountId);
}
