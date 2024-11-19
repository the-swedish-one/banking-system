package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.InsufficientFundsException;
import com.bankingsystem.exception.OverdraftLimitExceededException;
import com.bankingsystem.model.JointCheckingAccount;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.persistence.JointCheckingAccountPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class JointCheckingAccountService {

    private final JointCheckingAccountPersistenceService jointCheckingAccountPersistenceService;
    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;

    public JointCheckingAccountService(JointCheckingAccountPersistenceService jointCheckingAccountPersistenceService, TransactionService transactionService, CurrencyConversionService currencyConversionService) {
        this.jointCheckingAccountPersistenceService = jointCheckingAccountPersistenceService;
        this.transactionService = transactionService;
        this.currencyConversionService = currencyConversionService;
    }

    // Deposit
    public JointCheckingAccount deposit(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        JointCheckingAccount jointCheckingAccount = jointCheckingAccountPersistenceService.getAccountById(accountId);
        jointCheckingAccount.setBalance(jointCheckingAccount.getBalance().add(amount));

        Transaction transaction = new Transaction(amount, null, accountId);
        transactionService.createTransaction(transaction);

        return jointCheckingAccountPersistenceService.updateAccount(jointCheckingAccount);
    }

    // Withdraw
    public JointCheckingAccount withdraw(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }

        JointCheckingAccount account = jointCheckingAccountPersistenceService.getAccountById(accountId);

        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
        if (availableBalance.compareTo(amount) < 0) {
            throw new OverdraftLimitExceededException("Withdrawal failed: Overdraft limit exceeded");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction(amount.negate(), accountId, null);
        transactionService.createTransaction(transaction);

        return jointCheckingAccountPersistenceService.updateAccount(account);
    }

    // Transfer
    public void transfer (BigDecimal amount, int fromAccountId, int toAccountId) {

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Transfer failed: Cannot transfer to the same account");
        }

        JointCheckingAccount fromAccount = getJointCheckingAccountById(fromAccountId);
        JointCheckingAccount toAccount = getJointCheckingAccountById(toAccountId);

        BigDecimal availableBalance = fromAccount.getBalance().add(fromAccount.getOverdraftLimit());
        if (availableBalance.compareTo(amount) < 0) {
            throw new OverdraftLimitExceededException("Transfer failed: Overdraft limit exceeded");
        }

        BigDecimal finalAmount = amount;
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            finalAmount = currencyConversionService.convertAmount(amount, fromAccount.getCurrency(), toAccount.getCurrency())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(finalAmount);
        jointCheckingAccountPersistenceService.updateAccount(fromAccount);
        jointCheckingAccountPersistenceService.updateAccount(toAccount);

        Transaction fromTransaction = new Transaction(amount.negate(), fromAccountId, toAccountId);
        Transaction toTransaction = new Transaction(finalAmount, fromAccountId, toAccountId);
        transactionService.createTransaction(fromTransaction);
        transactionService.createTransaction(toTransaction);
    }

    // Create new joint checking account
    public JointCheckingAccount createJointCheckingAccount(JointCheckingAccount account) {
        if (account.getOwner() == null || account.getBalance() == null || account.getCurrency() == null) {
            throw new IllegalArgumentException("Account creation failed: Missing required fields");
        }
        account.setIban(generateIBAN(account.getOwner().getPerson().getCountry()));
        return jointCheckingAccountPersistenceService.save(account);
    }

    // Get joint checking account by ID
    public JointCheckingAccount getJointCheckingAccountById(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return jointCheckingAccountPersistenceService.getAccountById(accountId);
    }

    // Get all joint checking accounts
    public List<JointCheckingAccount> getAllJointCheckingAccounts() {
        List<JointCheckingAccount> jointCheckingAccountList = jointCheckingAccountPersistenceService.getAllAccounts();
        if (jointCheckingAccountList.isEmpty()) {
            throw new AccountNotFoundException("No joint checking accounts found");
        }
        return jointCheckingAccountList;
    }

    // Update joint checking account
    public JointCheckingAccount updateJointCheckingAccount(JointCheckingAccount jointCheckingAccount) {
        return jointCheckingAccountPersistenceService.updateAccount(jointCheckingAccount);
    }

    // Delete joint checking account by ID
    public boolean deleteJointCheckingAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return jointCheckingAccountPersistenceService.deleteAccount(accountId);
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
