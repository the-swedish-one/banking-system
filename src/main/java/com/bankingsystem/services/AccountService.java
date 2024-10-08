package com.bankingsystem.services;

import bankingsystem.models.*;
import com.bankingsystem.models.*;
import com.bankingsystem.persistence.AccountPersistenceService;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.persistence.UserPersistenceService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountService {
    private final AccountPersistenceService accountPersistenceService;
    private final UserPersistenceService userPersistenceService;
    private final BankPersistenceService bankPersistenceService;
    private final CurrencyConversionService currencyConversionService;
    private final TransactionService transactionService;

    public AccountService(AccountPersistenceService accountPersistenceService, UserPersistenceService userPersistenceService, BankPersistenceService bankPersistenceService, CurrencyConversionService currencyConversionService, TransactionService transactionService) {
        this.accountPersistenceService = accountPersistenceService;
        this.userPersistenceService = userPersistenceService;
        this.bankPersistenceService = bankPersistenceService;
        this.currencyConversionService = currencyConversionService;
        this.transactionService = transactionService;
    }

    // Deposit
    public void deposit(String accountId, double amount) {
            Account account = getAccountById(accountId);
            account.deposit(amount);
            DepositTransaction transaction = transactionService.createDepositTransaction(amount);
            account.getTransactionHistory().add(transaction);
            accountPersistenceService.updateAccount(account);
    }

    // Withdraw
    public void withdraw(String accountId, double amount) {
            Account account = getAccountById(accountId);
            account.withdraw(amount);
            WithdrawTransaction transaction = transactionService.createWithdrawTransaction(amount);
            account.getTransactionHistory().add(transaction);
            accountPersistenceService.updateAccount(account);
    }

    // Transfer
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
            if (fromAccount.getCurrency() != toAccount.getCurrency()) {
                double convertedAmount = currencyConversionService.convertAmount(amount, fromAccount.getCurrency(), toAccount.getCurrency());
                BigDecimal bd = new BigDecimal(convertedAmount).setScale(2, RoundingMode.HALF_UP);
                double roundedConvertedAmount = bd.doubleValue();
                fromAccount.withdraw(amount);
                toAccount.deposit(roundedConvertedAmount);

                TransferTransaction fromTransaction = transactionService.createTransferTransaction(amount, fromAccountId, toAccountId);
                TransferTransaction toTransaction = transactionService.createTransferTransaction(roundedConvertedAmount, fromAccountId, toAccountId);
                fromAccount.getTransactionHistory().add(fromTransaction);
                toAccount.getTransactionHistory().add(toTransaction);
                accountPersistenceService.updateAccount(fromAccount);
                accountPersistenceService.updateAccount(toAccount);
            } else {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);

                TransferTransaction transaction = new TransferTransaction(amount, fromAccountId, toAccountId);
                fromAccount.getTransactionHistory().add(transaction);
                toAccount.getTransactionHistory().add(transaction);
                accountPersistenceService.updateAccount(fromAccount);
                accountPersistenceService.updateAccount(toAccount);
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
    public CheckingAccount createCheckingAccount(Bank bank, User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(owner, balance, currency, overdraftLimit);
        accountPersistenceService.createAccount(checkingAccount);
        owner.getAccounts().add(checkingAccount);
        userPersistenceService.updateUser(owner);
        bank.getAccounts().add(checkingAccount);
        bankPersistenceService.updateBank(bank);
        return checkingAccount;
    }

    // Create Savings Account
    public SavingsAccount createSavingsAccount(Bank bank, User owner, double balance, CurrencyCode currency, double interestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(owner, balance, currency, interestRate);
        accountPersistenceService.createAccount(savingsAccount);
        owner.getAccounts().add(savingsAccount);
        userPersistenceService.updateUser(owner);
        bank.getAccounts().add(savingsAccount);
        bankPersistenceService.updateBank(bank);
        return savingsAccount;
    }

    // Create Joint Checking Account
    public JointCheckingAccount createJointCheckingAccount(Bank bank, User owner, User secondOwner, double balance, CurrencyCode currency, double overdraftLimit) {
        JointCheckingAccount jointCheckingAccount = new JointCheckingAccount(owner, secondOwner, balance, currency, overdraftLimit);
        accountPersistenceService.createAccount(jointCheckingAccount);
        owner.getAccounts().add(jointCheckingAccount);
        secondOwner.getAccounts().add(jointCheckingAccount);
        userPersistenceService.updateUser(owner);
        userPersistenceService.updateUser(secondOwner);
        bank.getAccounts().add(jointCheckingAccount);
        bankPersistenceService.updateBank(bank);
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
        if (account instanceof CheckingAccount checkingAccount) {
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
