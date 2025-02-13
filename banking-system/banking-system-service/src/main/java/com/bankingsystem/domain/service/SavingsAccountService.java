package com.bankingsystem.domain.service;

import com.bankingsystem.domain.model.CheckingAccount;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.exception.InsufficientFundsException;
import com.bankingsystem.domain.model.SavingsAccount;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.persistence.SavingsAccountPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SavingsAccountService {

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccountService.class);

    private final SavingsAccountPersistenceService savingsAccountPersistenceService;
    private final UserPersistenceService userPersistenceService;
    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;

    public SavingsAccountService(SavingsAccountPersistenceService savingsAccountPersistenceService, UserPersistenceService userPersistenceService, TransactionService transactionService, CurrencyConversionService currencyConversionService) {
        this.savingsAccountPersistenceService = savingsAccountPersistenceService;
        this.userPersistenceService = userPersistenceService;
        this.transactionService = transactionService;
        this.currencyConversionService = currencyConversionService;
    }

    // Create new savings account
    public SavingsAccount createSavingsAccount(SavingsAccount account) {
        logger.info("Creating new savings account");
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
        logger.info("Successfully created new Savings Account with IBAN {}", account.getIban());
        return savingsAccountPersistenceService.save(account);
    }

    // Get savings account by ID
    public SavingsAccount getSavingsAccountById(int accountId) {
        logger.info("Fetching savings account by ID: {}", accountId);
        if (accountId <= 0) {
            logger.error("Invalid account ID: {}", accountId);
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return savingsAccountPersistenceService.getAccountById(accountId);
    }

    // Get all savings accounts
    public List<SavingsAccount> getAllSavingsAccounts() {
        logger.info("Fetching all savings accounts");
        return savingsAccountPersistenceService.getAllAccounts();
    }

    // Update savings account
    public SavingsAccount updateSavingsAccount(SavingsAccount savingsAccount) {
        logger.info("Updating savings account with ID: {}", savingsAccount.getAccountId());
        if (savingsAccount.getAccountId() <= 0) {
            logger.error("Update failed: Invalid Account ID");
            throw new IllegalArgumentException("Update failed: Invalid Account ID");
        }
        return savingsAccountPersistenceService.updateAccount(savingsAccount);
    }

    // Delete savings account by ID
    public boolean deleteSavingsAccount(int accountId) {
        logger.info("Deleting savings account with ID: {}", accountId);
        if (accountId <= 0) {
            logger.error("Invalid account ID: {}", accountId);
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        try {
            boolean isDeleted = savingsAccountPersistenceService.deleteAccount(accountId);
            logger.info("Successfully deleted savings account for ID: {}", accountId);
            return isDeleted;
        } catch (AccountNotFoundException ex) {
            logger.error("Savings account not found for ID: {}", accountId);
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
    public SavingsAccount deposit(int accountId, BigDecimal amount) {
        logger.info("Depositing {} {} to account with ID: {}", amount, getSavingsAccountById(accountId).getCurrency(), accountId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Deposit failed: Amount must be greater than zero");
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        SavingsAccount savingsAccount = savingsAccountPersistenceService.getAccountById(accountId);
        savingsAccount.setBalance(savingsAccount.getBalance().add(amount));

        Transaction transaction = new Transaction(amount, null, savingsAccount.getIban());
        transactionService.createTransaction(transaction);

        logger.info("Successfully deposited {} {} to account with ID: {}", amount, savingsAccount.getCurrency(), accountId);
        return savingsAccountPersistenceService.updateAccount(savingsAccount);
    }

    // Withdraw
    public SavingsAccount withdraw(int accountId, BigDecimal amount) {
        logger.info("Withdrawing {} {} from account with ID: {}", amount, getSavingsAccountById(accountId).getCurrency(), accountId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Withdrawal failed: Amount must be greater than zero");
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }

        SavingsAccount account = savingsAccountPersistenceService.getAccountById(accountId);

        BigDecimal availableBalance = account.getBalance();
        if (availableBalance.compareTo(amount) < 0) {
            logger.error("Withdrawal failed: Insufficient funds");
            throw new InsufficientFundsException("Withdrawal failed: Insufficient funds");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction(amount.negate(), account.getIban(), null);
        transactionService.createTransaction(transaction);

        logger.info("Successfully withdrew {} {} from account with ID: {}", amount, account.getCurrency(), accountId);
        return savingsAccountPersistenceService.updateAccount(account);
    }

    // Transfer
    public List<SavingsAccount> transfer (BigDecimal amount, String fromAccountIban, String toAccountIban) {
        logger.info("Transferring {} from IBAN: {} to IBAN: {}", amount, fromAccountIban, toAccountIban);
        if (Objects.equals(fromAccountIban, toAccountIban)) {
            logger.error("Transfer failed: Cannot transfer to the same account");
            throw new IllegalArgumentException("Transfer failed: Cannot transfer to the same account");
        }

        SavingsAccount fromAccount = savingsAccountPersistenceService.getAccountByIban(fromAccountIban);
        SavingsAccount toAccount = savingsAccountPersistenceService.getAccountByIban(toAccountIban);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            logger.error("Transfer failed: Insufficient funds");
            throw new InsufficientFundsException("Transfer failed: Insufficient funds");
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

        SavingsAccount updatedFromAccount = savingsAccountPersistenceService.updateAccount(fromAccount);
        SavingsAccount updatedToAccount = savingsAccountPersistenceService.updateAccount(toAccount);

        Transaction fromTransaction = new Transaction(amount.negate(), fromAccountIban, toAccountIban);
        Transaction toTransaction = new Transaction(finalAmount, fromAccountIban, toAccountIban);
        transactionService.createTransaction(fromTransaction);
        transactionService.createTransaction(toTransaction);

        logger.info("Successfully transferred {} {} from IBAN: {} to IBAN: {}", amount, fromAccount.getCurrency(), fromAccountIban, toAccountIban);
        return List.of(updatedFromAccount, updatedToAccount);
    }

    // Apply interest
    public SavingsAccount addInterest(SavingsAccount savingsAccount) {
        logger.info("Applying interest to savings account with ID: {}", savingsAccount.getAccountId());
        BigDecimal interestRateDecimal = BigDecimal.valueOf(savingsAccount.getInterestRatePercentage()).divide(new BigDecimal("100"), 5, RoundingMode.HALF_UP);
        BigDecimal interest = savingsAccount.getBalance().multiply(interestRateDecimal);
        BigDecimal newBalance = savingsAccount.getBalance().add(interest);
        newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);
        savingsAccount.setBalance(newBalance);
        logger.info("Successfully applied interest to savings account with ID: {}", savingsAccount.getAccountId());
        return savingsAccountPersistenceService.updateAccount(savingsAccount);
    }
}
