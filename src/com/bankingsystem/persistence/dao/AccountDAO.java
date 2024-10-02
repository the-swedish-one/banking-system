package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Account;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private List<Account> accounts = new ArrayList<>();

    // Create a new account
    public void createAccount(Account account) {
        accounts.add(account);
    }

    // Get one account by ID
    public Account getAccountById(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
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
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    // Update account
    public void updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountId() == account.getAccountId()) {
                accounts.set(i, account);
                return;
            }
        }
    }

    // Delete an account
    public boolean deleteAccount(int accountId) {
        return accounts.removeIf(account -> account.getAccountId() == accountId);
    }
}
