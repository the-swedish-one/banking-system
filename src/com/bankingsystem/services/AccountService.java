package com.bankingsystem.services;

import com.bankingsystem.models.*;
import com.bankingsystem.persistence.AccountDAO;
import com.bankingsystem.persistence.UserDAO;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();
    private UserDAO userDAO = new UserDAO();

    public AccountService(AccountDAO accountDAO, UserDAO userDAO) {
        this.accountDAO = accountDAO;
        this.userDAO = userDAO;
    }

    // Deposit
    public void deposit(Account account, double amount) {
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountDAO.updateAccount(account);
    }

    // Withdraw
    public void withdraw(Account account, double amount) throws Exception {
        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            if (checkingAccount.getBalance() + checkingAccount.getOverdraftLimit() >= amount) {
                checkingAccount.setBalance(checkingAccount.getBalance() - amount);
            } else {
                throw new Exception("Overdraft limit exceeded");
            }
        } else {
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
            } else {
                throw new Exception("Insufficient funds");
            }
        }

        accountDAO.updateAccount(account);
    }

    // Savings Account: Apply interest
    public void addInterest(SavingsAccount savingsAccount) {
        double interest = savingsAccount.getBalance() * savingsAccount.getInterestRate();
        double newBalance = savingsAccount.getBalance() + interest;
        savingsAccount.setBalance(newBalance);
        accountDAO.updateAccount(savingsAccount);
    }

    // Create Checking Account
    public void createCheckingAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(accountId, IBAN, owner, balance, currency, overdraftLimit);
        accountDAO.createAccount(checkingAccount);
        owner.getAccounts().add(checkingAccount);
        userDAO.updateUser(owner);
    }

    // Create Savings Account
    public void createSavingsAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double interestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(accountId, IBAN, owner, balance, currency, interestRate);
        accountDAO.createAccount(savingsAccount);
        owner.getAccounts().add(savingsAccount);
        userDAO.updateUser(owner);
    }

    // Create Joint Checking Account
    public void createJointCheckingAccount(int accountId, String IBAN, User owner, User secondOwner, double balance, CurrencyCode currency, double overdraftLimit) {
        JointCheckingAccount jointCheckingAccount = new JointCheckingAccount(accountId, IBAN, owner, secondOwner, balance, currency, overdraftLimit);
        accountDAO.createAccount(jointCheckingAccount);
        owner.getAccounts().add(jointCheckingAccount);
        secondOwner.getAccounts().add(jointCheckingAccount);
        userDAO.updateUser(owner);
        userDAO.updateUser(secondOwner);
    }

    // Get account by ID
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    // Delete account by ID
    public boolean deleteAccount(int accountId) {
        return accountDAO.deleteAccount(accountId);
    }
}
