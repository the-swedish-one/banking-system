package com.bankingsystem.services;

import com.bankingsystem.models.*;
import java.util.List;

public class Bank {
    private String bic;
    private List<Person> persons;
    private List<User> users;
    private List<Account> accounts;

    public Bank(String bic) {
        this.bic = bic;
    }

    public void createPerson(String name, String email, String addressLine1, String addressLine2, String city, String country) {
        Person person = new Person(name, email, addressLine1, addressLine2, city, country);
        this.persons.add(person);
    }

    public void createUser(String userId, UserRole userRole, Person person) {
        User user = new User(userId, userRole, person);
        this.users.add(user);
    }

    public void createCheckingAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double overdraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(accountId, IBAN, owner, balance, currency, overdraftLimit);
        this.accounts.add(checkingAccount);
        owner.getAccounts().add(checkingAccount);
    }

    public void createSavingsAccount(int accountId, String IBAN, User owner, double balance, CurrencyCode currency, double interestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(accountId, IBAN, owner, balance, currency, interestRate);
        this.accounts.add(savingsAccount);
        owner.getAccounts().add(savingsAccount);
    }

    public void createJointCheckingAccount(int accountId, String IBAN, User owner, User secondOwner, double balance, CurrencyCode currency, double overdraftLimit) {
        JointCheckingAccount jointCheckingAccount = new JointCheckingAccount(accountId, IBAN, owner, secondOwner, balance, currency, overdraftLimit);
        this.accounts.add(jointCheckingAccount);
        owner.getAccounts().add(jointCheckingAccount);
        secondOwner.getAccounts().add(jointCheckingAccount);
    }

    public Account findAccount(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

}
