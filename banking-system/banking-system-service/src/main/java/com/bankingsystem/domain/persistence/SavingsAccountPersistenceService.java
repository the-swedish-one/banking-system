package com.bankingsystem.domain.persistence;
import com.bankingsystem.domain.model.SavingsAccount;

import java.util.List;

public interface SavingsAccountPersistenceService {

    // Create a new account
    SavingsAccount save(SavingsAccount account);

    // Get one account by ID
    SavingsAccount getAccountById(int accountId);

    // Get one account by IBAN
    SavingsAccount getAccountByIban(String iban);

    // Get all accounts
    List<SavingsAccount> getAllAccounts();

    // Update account
    SavingsAccount updateAccount(SavingsAccount account);

    // Delete an account
    boolean deleteAccount(int accountId);
}
