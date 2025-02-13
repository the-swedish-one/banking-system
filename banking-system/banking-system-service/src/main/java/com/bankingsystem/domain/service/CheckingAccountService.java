package com.bankingsystem.domain.service;

import com.bankingsystem.domain.config.OverdraftConfig;
import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.exception.OverdraftLimitExceededException;
import com.bankingsystem.domain.model.CheckingAccount;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.CheckingAccountPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CheckingAccountService {

    private static final Logger logger = LoggerFactory.getLogger(CheckingAccountService.class);

    private final OverdraftConfig overdraftConfig;

    private final CheckingAccountPersistenceService checkingAccountPersistenceService;
    private final UserPersistenceService userPersistenceService;
    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;
    private final BankService bankService;

    public CheckingAccountService(OverdraftConfig overdraftConfig, CheckingAccountPersistenceService checkingAccountPersistenceService, UserPersistenceService userPersistenceService, TransactionService transactionService, CurrencyConversionService currencyConversionService, BankService bankService) {
        this.overdraftConfig = overdraftConfig;
        this.checkingAccountPersistenceService = checkingAccountPersistenceService;
        this.userPersistenceService = userPersistenceService;
        this.transactionService = transactionService;
        this.currencyConversionService = currencyConversionService;
        this.bankService = bankService;
    }

    // Create new checking account
    public CheckingAccount createCheckingAccount(CheckingAccount account) {
        logger.info("Creating new CheckingAccount");
        if (account.getOwner() == null || account.getBalance() == null || account.getCurrency() == null) {
            logger.error("Account creation failed: Missing required fields");
            throw new IllegalArgumentException("Account creation failed: Missing required fields");
        }

        User user = account.getOwner();
        if (user.getUserId() != null) {
            logger.info("User ID found, fetching user with ID: {}", user.getUserId());
            user = userPersistenceService.getUserById(user.getUserId());
        } else {
            logger.error("User ID is required to create an account");
            throw new IllegalArgumentException("User ID is required to create an account.");
        }
        account.setOwner(user);

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
        checkingAccount.deposit(amount);

        Transaction transaction = new Transaction(amount, null, checkingAccount.getIban());
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
            logger.error("Withdrawal failed: Overdraft limit exceeded");
            throw new OverdraftLimitExceededException("Withdrawal failed: Overdraft limit exceeded");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
        account.withdraw(amount);

        Transaction transaction = new Transaction(amount.negate(), account.getIban(), null);
        transactionService.createTransaction(transaction);

        logger.info("Successfully withdrew {} {} from account with ID: {}", amount, account.getCurrency(), accountId);
        return checkingAccountPersistenceService.updateAccount(account);
    }

    // Transfer
    public List<CheckingAccount> transfer (BigDecimal amount, String fromAccountIban, String toAccountIban) {
        logger.info("Transferring {} from IBAN: {} to IBAN: {}", amount, fromAccountIban, toAccountIban);
        if (Objects.equals(fromAccountIban, toAccountIban)) {
            logger.error("Transfer failed: Cannot transfer to the same account");
            throw new IllegalArgumentException("Transfer failed: Cannot transfer to the same account");
        }

        CheckingAccount fromAccount = checkingAccountPersistenceService.getAccountByIban(fromAccountIban);
        CheckingAccount toAccount = checkingAccountPersistenceService.getAccountByIban(toAccountIban);

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

        amount = amount.setScale(2, RoundingMode.HALF_UP);
        fromAccount.withdraw(amount);
        toAccount.deposit(finalAmount);

        CheckingAccount updatedFromAccount = checkingAccountPersistenceService.updateAccount(fromAccount);
        CheckingAccount updatedToAccount = checkingAccountPersistenceService.updateAccount(toAccount);

        Transaction fromTransaction = new Transaction(amount.negate(), fromAccountIban, toAccountIban);
        Transaction toTransaction = new Transaction(finalAmount, fromAccountIban, toAccountIban);
        transactionService.createTransaction(fromTransaction);
        transactionService.createTransaction(toTransaction);

        logger.info("Successfully transferred {} {} from IBAN: {} to IBAN: {}", amount, fromAccount.getCurrency(), fromAccountIban, toAccountIban);
        return List.of(updatedFromAccount, updatedToAccount);
    }

    // Apply interest to overdrawn accounts
    public void applyInterestToOverdrawnAccounts() {
        List<CheckingAccount> overdrawnAccounts = checkingAccountPersistenceService.getOverdrawnAccounts();

        if (overdrawnAccounts.isEmpty()) {
            logger.info("No overdrawn checking accounts found");
            return;
        }

        BigDecimal interestRate = overdraftConfig.getInterestRate();
        Duration overdraftDuration = overdraftConfig.getOverdraftDuration();

        logger.info("Applying interest to {} overdrawn Checking Accounts", overdrawnAccounts.size());
        overdrawnAccounts.stream()
                .filter(account -> Duration.between(account.getOverdraftTimestamp(), Instant.now()).compareTo(overdraftDuration) >= 0)
                .forEach(account -> {
                    BigDecimal interest = account.applyOverdraftInterest(interestRate);
                    checkingAccountPersistenceService.updateAccount(account);

                    bankService.addCollectedInterest(interest);

                    Transaction interestTransaction = new Transaction(
                            interest.negate(),
                            account.getIban(),
                            "BANK"
                    );
                    transactionService.createTransaction(interestTransaction);
                });

        logger.info("Successfully applied interest to overdrawn Checking Accounts");
    }

}
