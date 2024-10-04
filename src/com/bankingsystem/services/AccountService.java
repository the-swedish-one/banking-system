package com.bankingsystem.services;

import com.bankingsystem.models.*;
import com.bankingsystem.models.exceptions.*;
import com.bankingsystem.persistence.AccountPersistenceService;
import com.bankingsystem.persistence.UserPersistenceService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountService {
    private final AccountPersistenceService accountPersistenceService;
    private final UserPersistenceService userPersistenceService;
    private final CurrencyConversionService currencyConversionService;

    public AccountService(AccountPersistenceService accountPersistenceService, UserPersistenceService userPersistenceService, CurrencyConversionService currencyConversionService) {
        this.accountPersistenceService = accountPersistenceService;
        this.userPersistenceService = userPersistenceService;
        this.currencyConversionService = currencyConversionService;
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
        } catch (InsufficientFundsException | OverdraftLimitExceededException e) {
            System.out.println("Withdraw failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Withdraw failed: " + e.getMessage());
        }
    }

    // Transfer
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
        try {
            if (fromAccount.getCurrency() != toAccount.getCurrency()) {
                double convertedAmount = currencyConversionService.convertAmount(amount, fromAccount.getCurrency(), toAccount.getCurrency());
                BigDecimal bd = new BigDecimal(convertedAmount).setScale(2, RoundingMode.HALF_UP);
                double roundedConvertedAmount = bd.doubleValue();
                fromAccount.withdraw(amount);
                toAccount.deposit(roundedConvertedAmount);

                TransferTransaction fromTransaction = new TransferTransaction(amount, fromAccountId, toAccountId);
                TransferTransaction toTransaction = new TransferTransaction(roundedConvertedAmount, fromAccountId, toAccountId);
                fromAccount.getTransactionHistory().add(fromTransaction);
                toAccount.getTransactionHistory().add(toTransaction);

            } else {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);

                TransferTransaction transaction = new TransferTransaction(amount, fromAccountId, toAccountId);
                fromAccount.getTransactionHistory().add(transaction);
                toAccount.getTransactionHistory().add(transaction);
            }

        } catch (InsufficientFundsException | OverdraftLimitExceededException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    // Savings Account: Apply interest
    public void addInterest(SavingsAccount savingsAccount) {
        double interestRateDecimal = savingsAccount.getInterestRatPercentage() / 100.0;
        double interest = savingsAccount.getBalance() * interestRateDecimal;
        double newBalance = savingsAccount.getBalance() + interest;
        savingsAccount.setBalance(newBalance);
        accountPersistenceService.updateAccount(savingsAccount);
    }

    // Get Transaction History by account ID
    public String getTransactionHistory(String accountId) {
        Account account = getAccountById(accountId);
        StringBuilder transactionHistory = new StringBuilder();
        for (Transaction transaction : account.getTransactionHistory()) {
            transactionHistory.append(transaction.toString()).append("\n");
        }
        return transactionHistory.toString();
    }

    // Create Checking Account
    // TODO - add account to banks list of accounts
    public CheckingAccount createCheckingAccount(User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(owner, balance, currency, overdraftLimit);
        accountPersistenceService.createAccount(checkingAccount);
        owner.getAccounts().add(checkingAccount);
        userPersistenceService.updateUser(owner);
        return checkingAccount;
    }

    // Create Savings Account
    // TODO - add account to banks list of accounts
    public SavingsAccount createSavingsAccount(User owner, double balance, CurrencyCode currency, double interestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(owner, balance, currency, interestRate);
        accountPersistenceService.createAccount(savingsAccount);
        owner.getAccounts().add(savingsAccount);
        userPersistenceService.updateUser(owner);
        return savingsAccount;
    }

    // Create Joint Checking Account
    // TODO - add account to banks list of accounts
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

    // Get balance by account ID
    public double getBalance(String accountId) {
        return accountPersistenceService.getAccountById(accountId).getBalance();
    }

    // Get overdraft limit by account ID
    public double getOverdraftLimit(String accountId) {
        Account account = accountPersistenceService.getAccountById(accountId);
        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            return checkingAccount.getOverdraftLimit();
        } else {
            return 0;
        }
    }

    // Delete account by ID
    public boolean deleteAccount(String accountId) {
        return accountPersistenceService.deleteAccount(accountId);
    }
}
