package com.domain.persistence;

import com.domain.model.JointCheckingAccount;

import java.util.List;

public interface JointCheckingAccountPersistenceService {

    // Create a new account
    JointCheckingAccount save(JointCheckingAccount account);

    // Get one account by ID
    JointCheckingAccount getAccountById(int accountId);

    // Get all accounts
    List<JointCheckingAccount> getAllAccounts();

    // Update account
    JointCheckingAccount updateAccount(JointCheckingAccount account);

    // Delete an account
    boolean deleteAccount(int accountId);
}
