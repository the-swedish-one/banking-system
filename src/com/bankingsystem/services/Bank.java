package com.bankingsystem.services;

import com.bankingsystem.models.*;
import java.util.List;

public class Bank {
    private String BIC;
    private List<User> users;
    private List<Account> accounts;

    public Bank(String BIC) {
        this.BIC = BIC;
    }

    public void createUser(String userId, UserType userType, String name, String email, String addressLine1, String addressLine2, String city, String country) {
        User user = new User(userId, userType, name, email, addressLine1, addressLine2, city, country);
        users.add(user);
    }

    public void createAccount(int accountId, String IBAN, User owner1, User owner2, double balance, CurrencyCode currency) {
        Account account = new Account(accountId, IBAN, owner1, owner2, balance, currency);
        accounts.add(account);
    }

    public Account findAccount(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

}
