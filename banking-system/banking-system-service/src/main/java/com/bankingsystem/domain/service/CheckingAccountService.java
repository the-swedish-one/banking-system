package com.bankingsystem.domain.service;

import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.exception.OverdraftLimitExceededException;
import com.bankingsystem.domain.model.CheckingAccount;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.persistence.CheckingAccountPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CheckingAccountService {

    private static final Logger logger = LoggerFactory.getLogger(CheckingAccountService.class);

    private final CheckingAccountPersistenceService checkingAccountPersistenceService;
    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;

    public CheckingAccountService(CheckingAccountPersistenceService checkingAccountPersistenceService, TransactionService transactionService, CurrencyConversionService currencyConversionService) {
        this.checkingAccountPersistenceService = checkingAccountPersistenceService;
        this.transactionService = transactionService;
        this.currencyConversionService = currencyConversionService;
    }

    // Create new checking account
    public CheckingAccount createCheckingAccount(CheckingAccount account) {
        logger.info("Creating new CheckingAccount");
        if (account.getOwner() == null || account.getBalance() == null || account.getCurrency() == null) {
            logger.error("Account creation failed: Missing required fields");
            throw new IllegalArgumentException("Account creation failed: Missing required fields");
        }
        account.setIban(generateIBAN(account.getOwner().getPerson().getCountry()));
        logger.info("Successfully created new CheckingAccount with IBAN {}", account.getIban());
        return checkingAccountPersistenceService.save(account);
    }

    // Get checking account by ID
    public CheckingAccount getCheckingAccountById(int accountId) {
        logger.info("Fetching CheckingAccount by ID: {}", accountId);
        if (accountId <= 0) {
            logger.error("Invalid account ID: {}", accountId);
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return checkingAccountPersistenceService.getAccountById(accountId);
    }

    // Get all checking accounts
    public List<CheckingAccount> getAllCheckingAccounts() {
        logger.info("Fetching all checking accounts");
        return checkingAccountPersistenceService.getAllAccounts();
    }

    // Update checking account
    public CheckingAccount updateCheckingAccount(CheckingAccount checkingAccount) {
        logger.info("Updating checking account with ID: {}", checkingAccount.getAccountId());
        if (checkingAccount.getAccountId() <= 0) {
            logger.error("Update failed: Invalid Account ID");
            throw new IllegalArgumentException("Update failed: Invalid Account ID");
        }
        return checkingAccountPersistenceService.updateAccount(checkingAccount);
    }

    // Delete checking account by ID
    public boolean deleteCheckingAccount(int accountId) {
        logger.info("Deleting checking account with ID: {}", accountId);
        if (accountId <= 0) {
            logger.error("Invalid account ID: {}", accountId);
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        try {
            boolean isDeleted = checkingAccountPersistenceService.deleteAccount(accountId);
            logger.info("Successfully deleted checking account with ID: {}", accountId);
            return isDeleted;
        } catch (AccountNotFoundException ex) {
            logger.error("Checking account not found for ID: {}", accountId);
            throw ex;
        }
    }

    public String generateIBAN(String country) {
        logger.info("Generating IBAN");
        if (country == null || country.length() < 2) {
            logger.error("IBAN generation failed: Country is null or less than 2 characters");
            throw new IllegalArgumentException("Country is null or less than 2 characters");
        }
        String countryPrefix = country.substring(0, 2).toUpperCase();
        Random random = new Random();

        String randomDigits = IntStream.range(0, 14)
                .mapToObj(i -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());

        logger.info("Successfully generated IBAN");
        return countryPrefix + randomDigits;
    }

    // Deposit
    public CheckingAccount deposit(int accountId, BigDecimal amount) {
        logger.info("Depositing {} {} to account with ID: {}", amount, getCheckingAccountById(accountId).getCurrency(), accountId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Deposit failed: Amount must be greater than zero");
            throw new IllegalArgumentException("Deposit failed: Amount must be greater than zero");
        }
        CheckingAccount checkingAccount = checkingAccountPersistenceService.getAccountById(accountId);
        checkingAccount.setBalance(checkingAccount.getBalance().add(amount));

        Transaction transaction = new Transaction(amount, null, accountId);
        transactionService.createTransaction(transaction);

        logger.info("Successfully deposited {} {} to account with ID: {}", amount, checkingAccount.getCurrency(), accountId);
        return checkingAccountPersistenceService.updateAccount(checkingAccount);
    }

    // Withdraw
    public CheckingAccount withdraw(int accountId, BigDecimal amount) {
        logger.info("Withdrawing {} {} from account with ID: {}", amount, getCheckingAccountById(accountId).getCurrency(), accountId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Withdraw failed: Amount must be greater than zero");
            throw new IllegalArgumentException("Withdraw failed: Amount must be greater than zero");
        }

        CheckingAccount account = checkingAccountPersistenceService.getAccountById(accountId);

        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
        if (availableBalance.compareTo(amount) <= 0) {
            logger.error("Withdrawal failed: Amount must be greater than zero");
            throw new OverdraftLimitExceededException("Withdrawal failed: Overdraft limit exceeded");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction(amount.negate(), accountId, null);
        transactionService.createTransaction(transaction);

        logger.info("Successfully withdrew {} {} from account with ID: {}", amount, account.getCurrency(), accountId);
        return checkingAccountPersistenceService.updateAccount(account);
    }

    // Transfer
    public void transfer (BigDecimal amount, int fromAccountId, int toAccountId) {
        logger.info("Transferring {} {} from account with ID: {} to account with ID: {}", amount, getCheckingAccountById(fromAccountId).getCurrency(), fromAccountId, toAccountId);
        if (fromAccountId == toAccountId) {
            logger.error("Transfer failed: Cannot transfer to the same account");
            throw new IllegalArgumentException("Transfer failed: Cannot transfer to the same account");
        }

        CheckingAccount fromAccount = getCheckingAccountById(fromAccountId);
        CheckingAccount toAccount = getCheckingAccountById(toAccountId);

        BigDecimal availableBalance = fromAccount.getBalance().add(fromAccount.getOverdraftLimit());
        if (availableBalance.compareTo(amount) < 0) {
            logger.error("Transfer failed: Overdraft limit exceeded");
            throw new OverdraftLimitExceededException("Transfer failed: Overdraft limit exceeded");
        }

        BigDecimal finalAmount = amount;
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            finalAmount = currencyConversionService.convertAmount(amount, fromAccount.getCurrency(), toAccount.getCurrency())
                    .setScale(2, RoundingMode.HALF_UP);
            logger.info("Converted amount: {} {} to {} {}", amount, fromAccount.getCurrency(), finalAmount, toAccount.getCurrency());
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(finalAmount);
        checkingAccountPersistenceService.updateAccount(fromAccount);
        checkingAccountPersistenceService.updateAccount(toAccount);

        Transaction fromTransaction = new Transaction(amount.negate(), fromAccountId, toAccountId);
        Transaction toTransaction = new Transaction(finalAmount, fromAccountId, toAccountId);
        transactionService.createTransaction(fromTransaction);
        transactionService.createTransaction(toTransaction);

        logger.info("Successfully transferred {} {} from account with ID: {} to account with ID: {}", amount, fromAccount.getCurrency(), fromAccountId, toAccountId);
    }
}
