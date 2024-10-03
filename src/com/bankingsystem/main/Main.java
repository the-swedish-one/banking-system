package com.bankingsystem.main;

import com.bankingsystem.models.*;
import com.bankingsystem.persistence.*;
import com.bankingsystem.persistence.dao.*;
import com.bankingsystem.services.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Alex's Banking System");

        // Instantiate DAOs
        System.out.println("Instantiating DAOs");
        BankDAO bankDAO = new BankDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        CurrencyConversionDAO currencyConversionDAO = new CurrencyConversionDAO();
        PersonDAO personDAO = new PersonDAO();
        UserDAO userDAO = new UserDAO();

        // Instantiate Services
        System.out.println("Instantiating Services");
        BankService bankService = new BankService(bankDAO);
        AccountService accountService = new AccountService(accountDAO, userDAO);
        TransactionService transactionService = new TransactionService(transactionDAO);
        CurrencyConversionService currencyConversionService = new CurrencyConversionService(currencyConversionDAO);
        PersonService personService = new PersonService(personDAO);
        UserService userService = new UserService(userDAO);

        // Create a bank
        System.out.println("Creating a bank called BestBank");
        Bank bestBank = bankService.createBank("BestBank123");

        // Create two persons
        System.out.println("Creating a person called Alex");
        Person alex = personService.createPerson("Alex", "Damgaard", "alex@gmail.com", "Cosy Cottage", "1 Alex Street", "Alexville", "Alexland");

        System.out.println("Creating a person called Amee");
        Person amee = personService.createPerson("Amee", "Covarrubias", "amee@gmail.com", "Penthouse Suite", "1 Amee Boulevard", "Ameeville", "Ameeland");

        // Create two users
        System.out.println("Alex is becoming a user");
        User alexUser = userService.createUser(alex);

        System.out.println("Amee is becoming a user");
        User ameeUser = userService.createUser(amee);



    }
}