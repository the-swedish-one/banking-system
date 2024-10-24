package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Account;
import com.bankingsystem.persistence.AccountPersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class AccountDAO implements AccountPersistenceService {

    private List<Account> accounts = new ArrayList<>();

    // Create a new account
    @Override
    public Account save(Account account) {
        accounts.add(account);
        return account;
    }

    // Get one account by ID
    @Override
    public Account getAccountById(String accountId) {
        return accounts.stream()
                .filter(account -> Objects.equals(account.getAccountId(), accountId))
                .findFirst()
                .orElse(null);
    }

    // Get all accounts
    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    // Update account
    @Override
    public void updateAccount(Account account) {
        IntStream.range(0, accounts.size())
                .filter(i -> Objects.equals(accounts.get(i).getAccountId(), account.getAccountId()))
                .findFirst()
                .ifPresent(i -> accounts.set(i, account));
    }

    // Delete an account
    @Override
    public boolean deleteAccount(String accountId) {
        return accounts.removeIf(account -> Objects.equals(account.getAccountId(), accountId));
    }
}
