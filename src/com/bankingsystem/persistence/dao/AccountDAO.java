package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Account;
import com.bankingsystem.persistence.AccountPersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountDAO implements AccountPersistenceService {

    private List<Account> accounts = new ArrayList<>();

    // Create a new account
    @Override
    public void createAccount(Account account) {
        accounts.add(account);
    }

    // Get one account by ID
    @Override
    public Account getAccountById(String accountId) {
        for (Account account : accounts) {
            if (Objects.equals(account.getAccountId(), accountId)) {
                return account;
            }
        }
        return null;
    }
//    public Account getAccountById(int accountId) {
//        return accounts.stream()
//                .filter(account -> account.getAccountId() == accountId)
//                .findFirst()
//                .orElse(null);
//    }

    // Get all accounts
    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    // Update account
    @Override
    public void updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountId() == account.getAccountId()) {
                accounts.set(i, account);
                return;
            }
        }
    }

    // Delete an account
    @Override
    public boolean deleteAccount(String accountId) {
        return accounts.removeIf(account -> Objects.equals(account.getAccountId(), accountId));
    }
}
