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
        if (account.getBalance() >= amount) {
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
            accountDAO.updateAccount(account);
        } else {
            throw new Exception("Insufficient funds");
        }
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



}
