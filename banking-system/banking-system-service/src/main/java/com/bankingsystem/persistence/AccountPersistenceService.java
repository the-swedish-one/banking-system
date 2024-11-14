package com.bankingsystem.persistence;

import com.bankingsystem.model.AccountEntity;

import java.util.List;

public interface AccountPersistenceService {

    // Create a new account
    AccountEntity save(AccountEntity account);

    // Get one account by ID
    AccountEntity getAccountById(String accountId);

    // Get all accounts
    List<AccountEntity> getAllAccounts();

    // Update account
    void updateAccount(AccountEntity account);

    // Delete an account
    boolean deleteAccount(String accountId);
}
