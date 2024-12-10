package com.bankingsystem.domain.persistence;

import com.bankingsystem.domain.model.CheckingAccount;

import java.util.List;


public interface CheckingAccountPersistenceService {

    // Create a new account
    CheckingAccount save(CheckingAccount account);

    // Get one account by ID
    CheckingAccount getAccountById(int accountId);

    // Get one account by IBAN
    CheckingAccount getAccountByIban(String iban);

    // Get all accounts
    List<CheckingAccount> getAllAccounts();

    // Get all overdrawn accounts
    List<CheckingAccount> getOverdrawnAccounts();

    // Update account
    CheckingAccount updateAccount(CheckingAccount account);

    // Delete an account
    boolean deleteAccount(int accountId);

}
