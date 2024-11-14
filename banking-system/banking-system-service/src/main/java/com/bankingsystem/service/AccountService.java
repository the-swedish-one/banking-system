package com.bankingsystem.service;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.model.*;
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
    public void deposit(String accountId, BigDecimal amount) {
            Account account = getAccountById(accountId);
            account.deposit(amount);
            DepositTransaction transaction = transactionService.createDepositTransaction(amount);
            accountPersistenceService.updateAccount(account);
    }

    // Withdraw
    public void withdraw(String accountId, BigDecimal amount) {
            Account account = getAccountById(accountId);
            account.withdraw(amount);
            WithdrawTransaction transaction = transactionService.createWithdrawTransaction(amount);
            accountPersistenceService.updateAccount(account);
    }

    // Transfer
    public void transfer(BigDecimal amount, String fromAccountId, String toAccountId) {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
            if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
                BigDecimal convertedAmount = currencyConversionService.convertAmount(amount, fromAccount.getCurrency(), toAccount.getCurrency());
                convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);
                fromAccount.withdraw(amount);
                toAccount.deposit(convertedAmount);

                TransferTransaction fromTransaction = transactionService.createTransferTransaction(amount, fromAccountId, toAccountId);
                TransferTransaction toTransaction = transactionService.createTransferTransaction(convertedAmount, fromAccountId, toAccountId);
                accountPersistenceService.updateAccount(fromAccount);
                accountPersistenceService.updateAccount(toAccount);
            } else {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);

                TransferTransaction transaction = transactionService.createTransferTransaction(amount, fromAccountId, toAccountId);
                accountPersistenceService.updateAccount(fromAccount);
                accountPersistenceService.updateAccount(toAccount);
            }
    }

    // Savings Account: Apply interest
    public void addInterest(SavingsAccount savingsAccount) {
        BigDecimal interestRateDecimal = BigDecimal.valueOf(savingsAccount.getInterestRatePercentage()).divide(new BigDecimal("100"), 5, RoundingMode.HALF_UP);
        BigDecimal interest = savingsAccount.getBalance().multiply(interestRateDecimal);
        BigDecimal newBalance = savingsAccount.getBalance().add(interest);
        newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);
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
    public CheckingAccount createCheckingAccount(Bank bank, User owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(owner, balance, currency, overdraftLimit);
        accountPersistenceService.save(checkingAccount);
        owner.getAccounts().add(checkingAccount);
        userPersistenceService.updateUser(owner);
        bank.getAccounts().add(checkingAccount);
        bankPersistenceService.updateBank(bank);
        return checkingAccount;
    }

    // Create Savings Account
    public SavingsAccount createSavingsAccount(Bank bank, User owner, BigDecimal balance, CurrencyCode currency, double interestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(owner, balance, currency, interestRate);
        accountPersistenceService.save(savingsAccount);
        owner.getAccounts().add(savingsAccount);
        userPersistenceService.updateUser(owner);
        bank.getAccounts().add(savingsAccount);
        bankPersistenceService.updateBank(bank);
        return savingsAccount;
    }

    // Create Joint Checking Account
    public JointCheckingAccount createJointCheckingAccount(Bank bank, User owner, User secondOwner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        JointCheckingAccount jointCheckingAccount = new JointCheckingAccount(owner, secondOwner, balance, currency, overdraftLimit);
        accountPersistenceService.save(jointCheckingAccount);
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
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        Account account = accountPersistenceService.getAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        return account;
    }

    // Get balance by account ID
    public BigDecimal getBalance(String accountId) {
        return accountPersistenceService.getAccountById(accountId).getBalance();
    }

    // Get overdraft limit by account ID
    public BigDecimal getOverdraftLimit(String accountId) {
        Account account = accountPersistenceService.getAccountById(accountId);
        if (account instanceof CheckingAccount checkingAccount) {
            return checkingAccount.getOverdraftLimit();
        } else {
            return new BigDecimal("0");
        }
    }

    // Delete account by ID
    public boolean deleteAccount(String accountId) {
        if(accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        return accountPersistenceService.deleteAccount(accountId);
    }
}
