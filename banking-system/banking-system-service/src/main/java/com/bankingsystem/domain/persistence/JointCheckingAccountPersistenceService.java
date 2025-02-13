package com.bankingsystem.domain.persistence;

import com.bankingsystem.domain.model.JointCheckingAccount;

import java.util.List;

public interface JointCheckingAccountPersistenceService {

    // Create a new account
    JointCheckingAccount save(JointCheckingAccount account);

    // Get one account by ID
    JointCheckingAccount getAccountById(int accountId);

    // Get one account by IBAN
    JointCheckingAccount getAccountByIban(String iban);

    // Get all accounts
    List<JointCheckingAccount> getAllAccounts();

    // Get all overdrawn accounts
    List<JointCheckingAccount> getOverdrawnAccounts();

    // Update account
    JointCheckingAccount updateAccount(JointCheckingAccount account);

    // Delete an account
    boolean deleteAccount(int accountId);
}
