package com.bankingsystem.models;

import java.util.List;

public class User {
    private String userId;
    private UserType userType; // TODO change this to a class with a set of user roles
    private Person person;
    private List<Account> accounts;

    public User(String userId, UserType userType, Person person) {
        this.userId = userId;
        this.userType = userType;
        this.person = person;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String newUserId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public void setUserType(String newUserType) {
        this.userId = newUserType;
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

