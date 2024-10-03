package com.bankingsystem.persistence;

import com.bankingsystem.models.Account;

import java.util.List;

public interface AccountPersistenceService {

    // Create a new account
    void createAccount(Account account);

    // Get one account by ID
    Account getAccountById(String accountId);

    // Get all accounts
    List<Account> getAllAccounts();

    // Update account
    void updateAccount(Account account);

    // Delete an account
    boolean deleteAccount(String accountId);
}
