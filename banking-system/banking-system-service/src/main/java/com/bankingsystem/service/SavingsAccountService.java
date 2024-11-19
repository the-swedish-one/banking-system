package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.InsufficientFundsException;
import com.bankingsystem.model.SavingsAccount;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.persistence.SavingsAccountPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SavingsAccountService {

    private final SavingsAccountPersistenceService savingsAccountPersistenceService;
    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;

    public SavingsAccountService(SavingsAccountPersistenceService savingsAccountPersistenceService, TransactionService transactionService, CurrencyConversionService currencyConversionService) {
        this.savingsAccountPersistenceService = savingsAccountPersistenceService;
        this.transactionService = transactionService;
        this.currencyConversionService = currencyConversionService;
    }

    // Create new savings account
    public SavingsAccount createSavingsAccount(SavingsAccount account) {
        if (account.getOwner() == null || account.getBalance() == null || account.getCurrency() == null) {
            throw new IllegalArgumentException("Account creation failed: Missing required fields");
        }
        account.setIban(generateIBAN(account.getOwner().getPerson().getCountry()));
        return savingsAccountPersistenceService.save(account);
    }

    // Get savings account by ID
    public SavingsAccount getSavingsAccountById(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return savingsAccountPersistenceService.getAccountById(accountId);
    }

    // Get all savings accounts
    public List<SavingsAccount> getAllSavingsAccounts() {
        List<SavingsAccount> savingsAccountList = savingsAccountPersistenceService.getAllAccounts();
        if (savingsAccountList.isEmpty()) {
            throw new AccountNotFoundException("No savings accounts found");
        }
        return savingsAccountList;
    }

    // Update savings account
    public SavingsAccount updateSavingsAccount(SavingsAccount savingsAccount) {
        return savingsAccountPersistenceService.updateAccount(savingsAccount);
    }

    // Delete savings account by ID
    public boolean deleteSavingsAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return savingsAccountPersistenceService.deleteAccount(accountId);
    }

    public String generateIBAN(String country) {
        if (country == null || country.length() < 2) {
            throw new IllegalArgumentException("Invalid country code for IBAN generation");
        }
        String countryPrefix = country.substring(0, 2).toUpperCase();
        Random random = new Random();

        String randomDigits = IntStream.range(0, 14)
                .mapToObj(i -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());

        return countryPrefix + randomDigits;
    }

    // Deposit
    public SavingsAccount deposit(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        SavingsAccount savingsAccount = savingsAccountPersistenceService.getAccountById(accountId);
        savingsAccount.setBalance(savingsAccount.getBalance().add(amount));

        Transaction transaction = new Transaction(amount, null, accountId);
        transactionService.createTransaction(transaction);

        return savingsAccountPersistenceService.updateAccount(savingsAccount);
    }

    // Withdraw
    public SavingsAccount withdraw(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }

        SavingsAccount account = savingsAccountPersistenceService.getAccountById(accountId);

        BigDecimal availableBalance = account.getBalance();
        if (availableBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Withdrawal failed: Overdraft limit exceeded");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction(amount.negate(), accountId, null);
        transactionService.createTransaction(transaction);

        return savingsAccountPersistenceService.updateAccount(account);
    }

    // Transfer
    public void transfer (BigDecimal amount, int fromAccountId, int toAccountId) {

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Transfer failed: Cannot transfer to the same account");
        }

        SavingsAccount fromAccount = getSavingsAccountById(fromAccountId);
        SavingsAccount toAccount = getSavingsAccountById(toAccountId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Transfer failed: Insufficient funds");
        }

        BigDecimal finalAmount = amount;
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            finalAmount = currencyConversionService.convertAmount(amount, fromAccount.getCurrency(), toAccount.getCurrency())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(finalAmount);
        savingsAccountPersistenceService.updateAccount(fromAccount);
        savingsAccountPersistenceService.updateAccount(toAccount);

        Transaction fromTransaction = new Transaction(amount.negate(), fromAccountId, toAccountId);
        Transaction toTransaction = new Transaction(finalAmount, fromAccountId, toAccountId);
        transactionService.createTransaction(fromTransaction);
        transactionService.createTransaction(toTransaction);
    }

    // Apply interest
    public SavingsAccount addInterest(SavingsAccount savingsAccount) {
        BigDecimal interestRateDecimal = BigDecimal.valueOf(savingsAccount.getInterestRatePercentage()).divide(new BigDecimal("100"), 5, RoundingMode.HALF_UP);
        BigDecimal interest = savingsAccount.getBalance().multiply(interestRateDecimal);
        BigDecimal newBalance = savingsAccount.getBalance().add(interest);
        newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);
        savingsAccount.setBalance(newBalance);
        return savingsAccountPersistenceService.updateAccount(savingsAccount);
    }
}
