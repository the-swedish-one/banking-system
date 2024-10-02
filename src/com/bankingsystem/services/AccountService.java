package com.bankingsystem.services;

import com.bankingsystem.models.*;
import com.bankingsystem.persistence.AccountPersistenceService;
import com.bankingsystem.persistence.UserPersistenceService;

public class AccountService {
    private final AccountPersistenceService accountPersistenceService;
    private final UserPersistenceService userPersistenceService;

    public AccountService(AccountPersistenceService accountPersistenceService, UserPersistenceService userPersistenceService) {
        this.accountPersistenceService = accountPersistenceService;
        this.userPersistenceService = userPersistenceService;
    }

    // Deposit
    public void deposit(Account account, double amount) {
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountPersistenceService.updateAccount(account);
    }

    // Withdraw
    public void withdraw(Account account, double amount) throws Exception {
        account.withdraw(amount);
        accountPersistenceService.updateAccount(account);
//        if (account instanceof CheckingAccount) {
//            CheckingAccount checkingAccount = (CheckingAccount) account;
//            if (checkingAccount.getBalance() + checkingAccount.getOverdraftLimit() >= amount) {
//                checkingAccount.setBalance(checkingAccount.getBalance() - amount);
//            } else {
//                throw new Exception("Overdraft limit exceeded");
//            }
//        } else {
//            if (account.getBalance() >= amount) {
//                account.setBalance(account.getBalance() - amount);
//            } else {
//                throw new Exception("Insufficient funds");
//            }
//        }
    }

    // Savings Account: Apply interest
    public void addInterest(SavingsAccount savingsAccount) {
        double interest = savingsAccount.getBalance() * savingsAccount.getInterestRate();
        double newBalance = savingsAccount.getBalance() + interest;
        savingsAccount.setBalance(newBalance);
        accountPersistenceService.updateAccount(savingsAccount);
    }

    // Create Checking Account
    public void createCheckingAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(accountId, IBAN, owner, balance, currency, overdraftLimit);
        accountPersistenceService.createAccount(checkingAccount);
        owner.getAccounts().add(checkingAccount);
        userPersistenceService.updateUser(owner);
    }

    // Create Savings Account
    public void createSavingsAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double interestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(accountId, IBAN, owner, balance, currency, interestRate);
        accountPersistenceService.createAccount(savingsAccount);
        owner.getAccounts().add(savingsAccount);
        userPersistenceService.updateUser(owner);
    }

    // Create Joint Checking Account
    public void createJointCheckingAccount(int accountId, String IBAN, User owner, User secondOwner, double balance, CurrencyCode currency, double overdraftLimit) {
        JointCheckingAccount jointCheckingAccount = new JointCheckingAccount(accountId, IBAN, owner, secondOwner, balance, currency, overdraftLimit);
        accountPersistenceService.createAccount(jointCheckingAccount);
        owner.getAccounts().add(jointCheckingAccount);
        secondOwner.getAccounts().add(jointCheckingAccount);
        userPersistenceService.updateUser(owner);
        userPersistenceService.updateUser(secondOwner);
    }

    // Get account by ID
    public Account getAccountById(int accountId) {
        return accountPersistenceService.getAccountById(accountId);
    }

    // Delete account by ID
    public boolean deleteAccount(int accountId) {
        return accountPersistenceService.deleteAccount(accountId);
    }
}
