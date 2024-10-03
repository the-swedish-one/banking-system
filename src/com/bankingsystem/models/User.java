package com.bankingsystem.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String userId;
    private Person person;
    private List<Account> accounts;

    public User(Person person) {
        this.userId = "user-" + UUID.randomUUID();
        this.person = person;
        this.accounts = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
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
}

