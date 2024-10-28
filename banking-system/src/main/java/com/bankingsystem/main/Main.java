package com.bankingsystem.main;

import com.bankingsystem.models.*;
import com.bankingsystem.persistence.dao.*;
import com.bankingsystem.service.*;
import com.bankingsystem.models.exceptions.InsufficientFundsException;
import com.bankingsystem.models.exceptions.OverdraftLimitExceededException;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Alex's Banking System");

        // Instantiate DAOs
        System.out.println("Instantiating DAOs");
        BankDAO bankDAO = new BankDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        CurrencyConversionDAO currencyConversionDAO = new CurrencyConversionDAO();
        PersonDetailsDAO personDAO = new PersonDetailsDAO();
        UserDAO userDAO = new UserDAO();

        // Instantiate Services
        System.out.println("Instantiating Services");
        BankService bankService = new BankService(bankDAO);
        CurrencyConversionService currencyConversionService = new CurrencyConversionService(currencyConversionDAO);
        TransactionService transactionService = new TransactionService(transactionDAO);
        AccountService accountService = new AccountService(accountDAO, userDAO, bankDAO, currencyConversionService, transactionService);
        PersonDetailsService personService = new PersonDetailsService(personDAO);
        UserService userService = new UserService(userDAO, bankDAO);

        // Create a bank
        System.out.println("Creating a bank called BestBank");
        Bank bestBank = bankService.createBank("Best Bank", "BESBA123");

        // Create persons
        System.out.println("Creating persons called Alex, Amee, Nicolas");
        PersonDetails alex = personService.createPersonDetails("Alex", "The Bestest", "alex@gmail.com", "Cosy Cottage", "1 Alex Street", "Alexville", "Alexland");

        PersonDetails amee = personService.createPersonDetails("Amee", "The Greatest", "amee@gmail.com", "Penthouse Suite", "1 Amee Boulevard", "Ameeville", "Ameeland");

        PersonDetails nicolas = personService.createPersonDetails("Nicolas", "The Coolest", "nicolas@gmail.com", "Coolest House", "1 Nicolas Avenue", "Nicville", "Nicland");

        // Create users
        System.out.println("Making Persons into Users");
        User alexUser = userService.createUser(bestBank, alex);

        User ameeUser = userService.createUser(bestBank, amee);

        User nicolasUser = userService.createUser(bestBank, nicolas);

        System.out.println("***************************");

        // Create accounts
        System.out.println("Creating Alex's checking and savings accounts");
        CheckingAccount alexCheckingAccount = accountService.createCheckingAccount(bestBank, alexUser, new BigDecimal("0"), CurrencyCode.EUR, BigDecimal.valueOf(1000.00));

        SavingsAccount alexSavingsAccount = accountService.createSavingsAccount(bestBank, alexUser, new BigDecimal("0"), CurrencyCode.EUR, 1.5);

        System.out.println("Creating Amee's checking and savings accounts");
        CheckingAccount ameeCheckingAccount = accountService.createCheckingAccount(bestBank, ameeUser, new BigDecimal("0"), CurrencyCode.USD, BigDecimal.valueOf(2000.00));

        SavingsAccount ameeSavingsAccount = accountService.createSavingsAccount(bestBank, ameeUser, new BigDecimal("0"), CurrencyCode.USD, 2.0);

        System.out.println("Creating Nicolas's checking account");
        CheckingAccount nicolasCheckingAccount = accountService.createCheckingAccount(bestBank, nicolasUser, new BigDecimal("0"), CurrencyCode.EUR, BigDecimal.valueOf(1000.00));

        System.out.println("Creating a joint checking account for Alex and Amee");
        JointCheckingAccount alexAmeeJointCheckingAccount = accountService.createJointCheckingAccount(bestBank, alexUser, ameeUser, new BigDecimal("0"), CurrencyCode.SEK, BigDecimal.valueOf(3000.00));

        System.out.println("***************************");

        // Deposit money into Alex's accounts
        System.out.println("Depositing 1000 EUR into Alex's checking account");
        accountService.deposit(alexCheckingAccount.getAccountId(), BigDecimal.valueOf(1000.00));

        System.out.println("Depositing 2000 EUR into Alex's savings account");
        accountService.deposit(alexSavingsAccount.getAccountId(), BigDecimal.valueOf(2000.00));

        // Withdraw money from Alex's checking account
        System.out.println("Withdrawing 500 EUR from Alex's checking account");
        accountService.withdraw(alexCheckingAccount.getAccountId(), BigDecimal.valueOf(500.00));

        // Check balance of Alex's checking account
        BigDecimal alexCheckingAccountBalance = accountService.getBalance(alexCheckingAccount.getAccountId());
        CurrencyCode alexCheckingAccountCurrency = accountService.getAccountById(alexCheckingAccount.getAccountId()).getCurrency();
        System.out.println("Alex's checking account balance is now: " + alexCheckingAccountBalance + " " + alexCheckingAccountCurrency);

        // Check overdraft limit of Alex's checking account
        BigDecimal alexCheckingAccountOverdraftLimit = accountService.getOverdraftLimit(alexCheckingAccount.getAccountId());
        System.out.println("Alex's Checking account overdraft limit: " + alexCheckingAccountOverdraftLimit);

        // Demonstrate exception: Attempt to withdraw more money than is available Alex's checking account
        System.out.println("Demonstrate exception: Attempting to withdraw 2000 EUR from Alex's checking account");
        try {
            accountService.withdraw(alexCheckingAccount.getAccountId(), BigDecimal.valueOf(2000.00));
        } catch (OverdraftLimitExceededException e) {
            System.out.println(e.getMessage());
        }

        // Check balance of Alex's savings account
        BigDecimal alexSavingsAccountBalance = accountService.getBalance(alexSavingsAccount.getAccountId());
        CurrencyCode alexSavingsAccountCurrency = accountService.getAccountById(alexSavingsAccount.getAccountId()).getCurrency();
        System.out.println("Alex's savings account balance: " + alexSavingsAccountBalance + " " + alexSavingsAccountCurrency);

        // Demonstrate exception: Attempt to withdraw more money than is in Alex's savings account
        System.out.println("Demonstrate exception: Attempting to withdraw 3000 EUR from Alex's savings account");
        try {
            accountService.withdraw(alexSavingsAccount.getAccountId(), BigDecimal.valueOf(3000.00));
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("***************************");

        // Transfer money from Alex's checking account to Nicolas's checking account (same currencies)
        System.out.println("Transferring 500 EUR from Alex's checking account to Nicolas's checking account");
        accountService.transfer(BigDecimal.valueOf(500.00), alexCheckingAccount.getAccountId(), nicolasCheckingAccount.getAccountId());

        // Confirm amounts in Alex's and Nicolas's checking accounts
        alexCheckingAccountBalance = accountService.getBalance(alexCheckingAccount.getAccountId());
        alexCheckingAccountCurrency = accountService.getAccountById(alexCheckingAccount.getAccountId()).getCurrency();
        System.out.println("Alex's checking account balance after transfer: " + alexCheckingAccountBalance + " " + alexCheckingAccountCurrency);

        BigDecimal nicolasCheckingAccountBalance = accountService.getBalance(nicolasCheckingAccount.getAccountId());
        CurrencyCode nicolasCheckingAccountCurrency = accountService.getAccountById(nicolasCheckingAccount.getAccountId()).getCurrency();
        System.out.println("Nicolas's checking account balance after transfer: " + nicolasCheckingAccountBalance + " " + nicolasCheckingAccountCurrency);

        // Transfer money from Alex's checking account to Nicolas's checking account (go into Alex's overdraft)
        System.out.println("Transferring 500 EUR from Alex's checking account (overdraft) to Nicolas's checking account");
        accountService.transfer(BigDecimal.valueOf(500.00), alexCheckingAccount.getAccountId(), nicolasCheckingAccount.getAccountId());

        // Confirm amounts in Alex's checking account
        alexCheckingAccountBalance = accountService.getBalance(alexCheckingAccount.getAccountId());
        alexCheckingAccountCurrency = accountService.getAccountById(alexCheckingAccount.getAccountId()).getCurrency();
        System.out.println("Alex's checking account balance after transfer: " + alexCheckingAccountBalance + " " + alexCheckingAccountCurrency);

        System.out.println("***************************");

        // Deposit money into Amee's accounts
        System.out.println("Depositing 2000 USD into Amee's checking account");
        accountService.deposit(ameeCheckingAccount.getAccountId(), BigDecimal.valueOf(2000.00));

        System.out.println("Depositing 3000 USD into Amee's savings account");
        accountService.deposit(ameeSavingsAccount.getAccountId(), BigDecimal.valueOf(3000.00));

        // Transfer money from Amee's checking account to Amee's checking account
        System.out.println("Transferring 1000 USD from Amee's checking account to Amee's savings account");
        accountService.transfer(BigDecimal.valueOf(1000.00), ameeCheckingAccount.getAccountId(), ameeSavingsAccount.getAccountId());

        // Confirm amounts in Amee's accounts
        BigDecimal ameeCheckingAccountBalance = accountService.getBalance(ameeCheckingAccount.getAccountId());
        CurrencyCode ameeCheckingAccountCurrency = accountService.getAccountById(ameeCheckingAccount.getAccountId()).getCurrency();
        System.out.println("Amee's checking account balance after transfer: " + ameeCheckingAccountBalance + " " + ameeCheckingAccountCurrency);

        BigDecimal ameeSavingsAccountBalance = accountService.getBalance(ameeSavingsAccount.getAccountId());
        CurrencyCode ameeSavingsAccountCurrency = accountService.getAccountById(ameeSavingsAccount.getAccountId()).getCurrency();
        System.out.println("Amee's savings account balance after balance: " + ameeSavingsAccountBalance + " " + ameeSavingsAccountCurrency);

        // Demonstrate exception: Attempt to transfer more money than is in Amee's checking account
        System.out.println("Demonstrate exception: Attempting to transfer 10000 USD from Amee's checking account to Amee's savings account");
        try {
            accountService.transfer(BigDecimal.valueOf(10000.00), ameeCheckingAccount.getAccountId(), ameeSavingsAccount.getAccountId());
        } catch (OverdraftLimitExceededException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("***************************");


        // Get exchange rate for USD to SEK
        double usdToSekRate = currencyConversionService.getExchangeRate(CurrencyCode.USD, CurrencyCode.SEK);
        System.out.println("USD to SEK exchange rate: " + usdToSekRate);

        // Perform currency conversion
        BigDecimal convertedAmount = currencyConversionService.convertAmount(BigDecimal.valueOf(500.00), CurrencyCode.USD, CurrencyCode.SEK);
        System.out.println("500 USD exchanges to: " + convertedAmount + " SEK");

        // Transfer money from Amee's checking account to Alex & Amee's joint checking account (different currencies) and demonstrate rounding
        System.out.println("Transferring 500 USD from Amee's checking account to Alex and Amee's joint checking account (SEK)");
        accountService.transfer(BigDecimal.valueOf(500.00), ameeCheckingAccount.getAccountId(), alexAmeeJointCheckingAccount.getAccountId());

        // Confirm amounts in Amee's checking account
        ameeCheckingAccountBalance = accountService.getBalance(ameeCheckingAccount.getAccountId());
        ameeCheckingAccountCurrency = accountService.getAccountById(ameeCheckingAccount.getAccountId()).getCurrency();
        System.out.println("Amee's checking account balance after transfer: " + ameeCheckingAccountBalance + " " + ameeCheckingAccountCurrency);

        // Confirm amounts in Alex & Amee's joint checking account
        BigDecimal alexAmeeJointCheckingAccountBalance = accountService.getBalance(alexAmeeJointCheckingAccount.getAccountId());
        CurrencyCode alexAmeeJointCheckingAccountCurrency = accountService.getAccountById(alexAmeeJointCheckingAccount.getAccountId()).getCurrency();
        System.out.println("Alex and Amee's joint checking account balance after transfer: " + alexAmeeJointCheckingAccountBalance + " " + alexAmeeJointCheckingAccountCurrency);


        System.out.println("***************************");

        // Check balance of Amee's savings account
        ameeSavingsAccountBalance = accountService.getBalance(ameeSavingsAccount.getAccountId());
        ameeSavingsAccountCurrency = accountService.getAccountById(ameeSavingsAccount.getAccountId()).getCurrency();
        System.out.println("Amee's savings account balance before interest: " + ameeSavingsAccountBalance + " " + ameeSavingsAccountCurrency);


        // Apply interest to Amee's savings account
        System.out.println("Applying interest to Amee's savings account");
        accountService.addInterest(ameeSavingsAccount);

        // Check balance of Amee's savings account
        ameeSavingsAccountBalance = accountService.getBalance(ameeSavingsAccount.getAccountId());
        ameeSavingsAccountCurrency = accountService.getAccountById(ameeSavingsAccount.getAccountId()).getCurrency();
        System.out.println("Amee's savings account balance after interest: " + ameeSavingsAccountBalance + " " + ameeSavingsAccountCurrency);


        System.out.println("***************************");

        // Get list of Users of bank
        System.out.println("Getting list of Users of Bank");
        String bankUsers = bankService.getAllBankUsers("BESBA123");
        System.out.println(bankUsers);

        // Get list of Accounts of bank
        System.out.println("Getting list of Accounts of Bank");
        String bankAccounts = bankService.getAllBankAccounts("BESBA123");
        System.out.println(bankAccounts);

        // Get Transaction History for Alex's checking account
        System.out.println("Getting transaction history for Alex's checking account");
        String alexCheckingAccountTransactionHistory = accountService.getTransactionHistory(alexCheckingAccount.getAccountId());
        System.out.println(alexCheckingAccountTransactionHistory);
    }
}