package com.bankingsystem.domain.service;

import com.bankingsystem.domain.config.OverdraftConfig;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.exception.OverdraftLimitExceededException;
import com.bankingsystem.domain.model.JointCheckingAccount;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.persistence.JointCheckingAccountPersistenceService;
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
public class JointCheckingAccountService {

    private static final Logger logger = LoggerFactory.getLogger(JointCheckingAccountService.class);

    private final OverdraftConfig overdraftConfig;

    private final JointCheckingAccountPersistenceService jointCheckingAccountPersistenceService;
    private final UserPersistenceService userPersistenceService;
    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;
    private final BankService bankService;

    public JointCheckingAccountService(OverdraftConfig overdraftConfig, JointCheckingAccountPersistenceService jointCheckingAccountPersistenceService, UserPersistenceService userPersistenceService, TransactionService transactionService, CurrencyConversionService currencyConversionService, BankService bankService) {
        this.overdraftConfig = overdraftConfig;
        this.jointCheckingAccountPersistenceService = jointCheckingAccountPersistenceService;
        this.userPersistenceService = userPersistenceService;
        this.transactionService = transactionService;
        this.currencyConversionService = currencyConversionService;
        this.bankService = bankService;
    }

    // Create new joint checking account
    public JointCheckingAccount createJointCheckingAccount(JointCheckingAccount account) {
        logger.info("Creating new joint checking account");
        if (account.getOwner() == null || account.getBalance() == null || account.getCurrency() == null) {
            logger.error("Account creation failed: Missing required fields");
            throw new IllegalArgumentException("Account creation failed: Missing required fields");
        }

        User user = account.getOwner();
        User secondUser = account.getSecondOwner();
        if (user.getUserId() != null && secondUser.getUserId() != null) {
            logger.info("User IDs found, fetching users with IDs: {} and {}", user.getUserId(), secondUser.getUserId());
            user = userPersistenceService.getUserById(user.getUserId());
            secondUser = userPersistenceService.getUserById(secondUser.getUserId());
        } else {
            logger.error("User ID is required to create an account");
            throw new IllegalArgumentException("User ID is required to create an account.");
        }
        account.setOwner(user);
        account.setSecondOwner(secondUser);

        account.setIban(generateIBAN(account.getOwner().getPerson().getCountry()));
        logger.info("Successfully created new Joint Checking Account with IBAN {}", account.getIban());
        return jointCheckingAccountPersistenceService.save(account);
    }

    // Get joint checking account by ID
    public JointCheckingAccount getJointCheckingAccountById(int accountId) {
        logger.info("Fetching joint checking account by ID: {}", accountId);
        if (accountId <= 0) {
            logger.error("Invalid account ID: {}", accountId);
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return jointCheckingAccountPersistenceService.getAccountById(accountId);
    }

    // Get all joint checking accounts
    public List<JointCheckingAccount> getAllJointCheckingAccounts() {
        logger.info("Fetching all joint checking accounts");
        return jointCheckingAccountPersistenceService.getAllAccounts();
    }

    // Update joint checking account
    public JointCheckingAccount updateJointCheckingAccount(JointCheckingAccount jointCheckingAccount) {
        logger.info("Updating joint checking account with ID: {}", jointCheckingAccount.getAccountId());
        if (jointCheckingAccount.getAccountId() <= 0) {
            logger.error("Update failed: Invalid Account ID");
            throw new IllegalArgumentException("Update failed: Invalid Account ID");
        }
        return jointCheckingAccountPersistenceService.updateAccount(jointCheckingAccount);
    }

    // Delete joint checking account by ID
    public boolean deleteJointCheckingAccount(int accountId) {
        logger.info("Deleting joint checking account with ID: {}", accountId);
        if (accountId <= 0) {
            logger.error("Invalid account ID: {}", accountId);
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        try{
            boolean isDeleted = jointCheckingAccountPersistenceService.deleteAccount(accountId);
            logger.info("Successfully deleted joint checking account for ID: {}", accountId);
            return isDeleted;
        } catch (AccountNotFoundException ex) {
            logger.error("Account not found for ID: {}", accountId);
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
    public JointCheckingAccount deposit(int accountId, BigDecimal amount) {
        logger.info("Depositing {} {} to account with ID: {}", amount, getJointCheckingAccountById(accountId).getCurrency(), accountId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Deposit failed: Amount must be greater than zero");
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        JointCheckingAccount jointCheckingAccount = jointCheckingAccountPersistenceService.getAccountById(accountId);
        jointCheckingAccount.deposit(amount);

        Transaction transaction = new Transaction(amount, null, jointCheckingAccount.getIban());
        transactionService.createTransaction(transaction);

        logger.info("Successfully deposited {} {} to account with ID: {}", amount, jointCheckingAccount.getCurrency(), accountId);
        return jointCheckingAccountPersistenceService.updateAccount(jointCheckingAccount);
    }

    // Withdraw
    public JointCheckingAccount withdraw(int accountId, BigDecimal amount) {
        logger.info("Withdrawing {} {} from account with ID: {}", amount, getJointCheckingAccountById(accountId).getCurrency(), accountId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Withdrawal failed: Amount must be greater than zero");
            throw new IllegalArgumentException("Withdrawal failed: Amount must be greater than zero");
        }

        JointCheckingAccount account = jointCheckingAccountPersistenceService.getAccountById(accountId);

        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit());
        if (availableBalance.compareTo(amount) < 0) {
            logger.error("Withdrawal failed: Overdraft limit exceeded");
            throw new OverdraftLimitExceededException("Withdrawal failed: Overdraft limit exceeded");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        account.withdraw(amount);

        Transaction transaction = new Transaction(amount.negate(), account.getIban(), null);
        transactionService.createTransaction(transaction);

        logger.info("Successfully withdrew {} {} from account with ID: {}", amount, account.getCurrency(), accountId);
        return jointCheckingAccountPersistenceService.updateAccount(account);
    }

    // Transfer
    public List<JointCheckingAccount> transfer (BigDecimal amount, String fromAccountIban, String toAccountIban) {
        logger.info("Transferring {} from IBAN: {} to IBAN: {}", amount, fromAccountIban, toAccountIban);
        if (Objects.equals(fromAccountIban, toAccountIban)) {
            logger.error("Transfer failed: Cannot transfer to the same account");
            throw new IllegalArgumentException("Transfer failed: Cannot transfer to the same account");
        }

        JointCheckingAccount fromAccount = jointCheckingAccountPersistenceService.getAccountByIban(fromAccountIban);
        JointCheckingAccount toAccount = jointCheckingAccountPersistenceService.getAccountByIban(toAccountIban);

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

        JointCheckingAccount updatedFromAccount = jointCheckingAccountPersistenceService.updateAccount(fromAccount);
        JointCheckingAccount updatedToAccount = jointCheckingAccountPersistenceService.updateAccount(toAccount);

        Transaction fromTransaction = new Transaction(amount.negate(), fromAccountIban, toAccountIban);
        Transaction toTransaction = new Transaction(finalAmount, fromAccountIban, toAccountIban);
        transactionService.createTransaction(fromTransaction);
        transactionService.createTransaction(toTransaction);

        logger.info("Successfully transferred {} {} from IBAN: {} to IBAN: {}", amount, fromAccount.getCurrency(), fromAccountIban, toAccountIban);
        return List.of(updatedFromAccount, updatedToAccount);
    }

    // Apply interest to overdrawn accounts
    public void applyInterestToOverdrawnAccounts() {
        List<JointCheckingAccount> overdrawnAccounts = jointCheckingAccountPersistenceService.getOverdrawnAccounts();

        if (overdrawnAccounts.isEmpty()) {
            logger.info("No overdrawn joint checking accounts found");
            return;
        }

        BigDecimal interestRate = overdraftConfig.getInterestRate();
        Duration overdraftDuration = overdraftConfig.getOverdraftDuration();

        logger.info("Applying interest to {} overdrawn Joint Checking Accounts", overdrawnAccounts.size());
        overdrawnAccounts.stream()
                .filter(account -> Duration.between(account.getOverdraftTimestamp(), Instant.now()).compareTo(overdraftDuration) >= 0)
                .forEach(account -> {
                    BigDecimal interest = account.applyOverdraftInterest(interestRate);
                    jointCheckingAccountPersistenceService.updateAccount(account);

                    bankService.addCollectedInterest(interest);

                    Transaction interestTransaction = new Transaction(
                            interest.negate(),
                            account.getIban(),
                            "BANK"
                    );
                    transactionService.createTransaction(interestTransaction);
                });

        logger.info("Successfully applied interest to overdrawn Joint Checking Accounts");
    }
}
