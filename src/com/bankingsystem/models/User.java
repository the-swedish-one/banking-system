package com.bankingsystem.models;

import java.util.List;
import java.util.UUID;

public class User {
    private String userId;
    private UserRole userRole;
    private Person person;
    private List<Account> accounts;

    public User(UserRole userRole, Person person) {
        this.userId = UUID.randomUUID().toString();
        this.userRole = userRole;
        this.person = person;
    }

    public String getUserId() {
        return userId;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public void setUserRole(UserRole newUserRole) {
        this.userRole = newUserRole;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> newAccounts) {
        this.accounts = newAccounts;
    }
}

