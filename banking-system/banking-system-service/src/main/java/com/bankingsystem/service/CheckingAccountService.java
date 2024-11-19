package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.InsufficientFundsException;
import com.bankingsystem.exception.OverdraftLimitExceededException;
import com.bankingsystem.model.CheckingAccount;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.persistence.CheckingAccountPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CheckingAccountService {

    private final CheckingAccountPersistenceService checkingAccountPersistenceService;
    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;

    public CheckingAccountService(CheckingAccountPersistenceService checkingAccountPersistenceService, TransactionService transactionService, CurrencyConversionService currencyConversionService) {
        this.checkingAccountPersistenceService = checkingAccountPersistenceService;
        this.transactionService = transactionService;
        this.currencyConversionService = currencyConversionService;
    }

    // Deposit
    public CheckingAccount deposit(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        CheckingAccount checkingAccount = checkingAccountPersistenceService.getAccountById(accountId);
        checkingAccount.setBalance(checkingAccount.getBalance().add(amount));

        Transaction transaction = new Transaction(amount, null, accountId);
        transactionService.createTransaction(transaction);

        return checkingAccountPersistenceService.updateAccount(checkingAccount);
    }

    // Withdraw
    public CheckingAccount withdraw(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }

        CheckingAccount account = checkingAccountPersistenceService.getAccountById(accountId);

        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
        if (availableBalance.compareTo(amount) < 0) {
            throw new OverdraftLimitExceededException("Withdrawal failed: Overdraft limit exceeded");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction(amount.negate(), accountId, null);
        transactionService.createTransaction(transaction);

        return checkingAccountPersistenceService.updateAccount(account);
    }

    // Transfer
    public void transfer (BigDecimal amount, int fromAccountId, int toAccountId) {

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Transfer failed: Cannot transfer to the same account");
        }

        CheckingAccount fromAccount = getCheckingAccountById(fromAccountId);
        CheckingAccount toAccount = getCheckingAccountById(toAccountId);

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
        checkingAccountPersistenceService.updateAccount(fromAccount);
        checkingAccountPersistenceService.updateAccount(toAccount);

        Transaction fromTransaction = new Transaction(amount.negate(), fromAccountId, toAccountId);
        Transaction toTransaction = new Transaction(finalAmount, fromAccountId, toAccountId);
        transactionService.createTransaction(fromTransaction);
        transactionService.createTransaction(toTransaction);
    }


    // Create new checking account
    public CheckingAccount createCheckingAccount(CheckingAccount account) {
        if (account.getOwner() == null || account.getBalance() == null || account.getCurrency() == null) {
            throw new IllegalArgumentException("Account creation failed: Missing required fields");
        }
        account.setIban(generateIBAN(account.getOwner().getPerson().getCountry()));
        return checkingAccountPersistenceService.save(account);
    }

    // Get checking account by ID
    public CheckingAccount getCheckingAccountById(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return checkingAccountPersistenceService.getAccountById(accountId);
    }

    // Get all checking accounts
    public List<CheckingAccount> getAllCheckingAccounts() {
        List<CheckingAccount> checkingAccountList = checkingAccountPersistenceService.getAllAccounts();
        if (checkingAccountList.isEmpty()) {
            throw new AccountNotFoundException("No checking accounts found");
        }
        return checkingAccountList;
    }

    // Update checking account
    public CheckingAccount updateCheckingAccount(CheckingAccount checkingAccount) {
        if (checkingAccount.getAccountId() <= 0) {
            throw new IllegalArgumentException("Update failed: Invalid Account ID");
        }
        return checkingAccountPersistenceService.updateAccount(checkingAccount);
    }

    // Delete checking account by ID
    public boolean deleteCheckingAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return checkingAccountPersistenceService.deleteAccount(accountId);
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
}
