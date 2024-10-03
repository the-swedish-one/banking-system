package com.bankingsystem.services;

import com.bankingsystem.models.*;
import com.bankingsystem.models.exceptions.*;
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
    public void deposit(String accountId, double amount) {
        try {
            Account account = getAccountById(accountId);
            account.deposit(amount);
            DepositTransaction transaction = new DepositTransaction(amount);
            account.getTransactionHistory().add(transaction);
            accountPersistenceService.updateAccount(account);
        } catch (RuntimeException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    // Withdraw
    public void withdraw(String accountId, double amount) {
        try {
            Account account = getAccountById(accountId);
            account.withdraw(amount);
            accountPersistenceService.updateAccount(account);
        } catch (InsufficientFundsException e) {
            System.out.println("Withdraw failed due to insufficient funds: " + e.getMessage());
        } catch (OverdraftLimitExceededException e) {
            System.out.println("Withdraw failed due to overdraft limit exceeded: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Withdraw failed: " + e.getMessage());
        }

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

    // Transfer
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
        try {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);

            TransferTransaction transaction = new TransferTransaction(amount, fromAccountId, toAccountId);
            fromAccount.getTransactionHistory().add(transaction);
            toAccount.getTransactionHistory().add(transaction);
        } catch (InsufficientFundsException e) {
            System.out.println("Transfer failed due to insufficient funds in sender account: " + e.getMessage());
        } catch (OverdraftLimitExceededException e) {
            System.out.println("Transfer failed due to overdraft limit exceeded in sender account: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    // Savings Account: Apply interest
    public void addInterest(SavingsAccount savingsAccount) {
        double interest = savingsAccount.getBalance() * savingsAccount.getInterestRate();
        double newBalance = savingsAccount.getBalance() + interest;
        savingsAccount.setBalance(newBalance);
        accountPersistenceService.updateAccount(savingsAccount);
    }

    // Create Checking Account
    public CheckingAccount createCheckingAccount(User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(owner, balance, currency, overdraftLimit);
        accountPersistenceService.createAccount(checkingAccount);
        owner.getAccounts().add(checkingAccount);
        userPersistenceService.updateUser(owner);
        return checkingAccount;
    }

    // Create Savings Account
    public SavingsAccount createSavingsAccount(User owner, double balance, CurrencyCode currency, double interestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(owner, balance, currency, interestRate);
        accountPersistenceService.createAccount(savingsAccount);
        owner.getAccounts().add(savingsAccount);
        userPersistenceService.updateUser(owner);
        return savingsAccount;
    }

    // Create Joint Checking Account
    public JointCheckingAccount createJointCheckingAccount(User owner, User secondOwner, double balance, CurrencyCode currency, double overdraftLimit) {
        JointCheckingAccount jointCheckingAccount = new JointCheckingAccount(owner, secondOwner, balance, currency, overdraftLimit);
        accountPersistenceService.createAccount(jointCheckingAccount);
        owner.getAccounts().add(jointCheckingAccount);
        secondOwner.getAccounts().add(jointCheckingAccount);
        userPersistenceService.updateUser(owner);
        userPersistenceService.updateUser(secondOwner);
        return jointCheckingAccount;
    }

    // Get account by ID
    public Account getAccountById(String accountId) {
        return accountPersistenceService.getAccountById(accountId);
    }

    // Delete account by ID
    public boolean deleteAccount(String accountId) {
        return accountPersistenceService.deleteAccount(accountId);
    }
}
