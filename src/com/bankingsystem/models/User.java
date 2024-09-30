package com.bankingsystem.models;

import java.util.List;

public class User {
    private String userId;
    private UserRole userRole;
    private Person person;
    private List<Account> accounts;

    public User(String userId, UserRole userRole, Person person) {
        this.userId = userId;
        this.userRole = userRole;
        this.person = person;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String newUserId) {
        this.userId = userId;
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

