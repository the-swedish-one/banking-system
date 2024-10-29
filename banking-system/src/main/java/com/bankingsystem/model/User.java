package com.bankingsystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String userId;
    private PersonDetails person;
    private List<Account> accounts;

    public User(PersonDetails person) {
        this.userId = "user-" + UUID.randomUUID();
        this.person = person;
        this.accounts = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public PersonDetails getPerson() {
        return this.person;
    }

    public void setPerson(PersonDetails newPerson) {
        this.person = newPerson;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + person.getFirstName() + '\'' +
                ", lastName='" + person.getLastName() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        // same object
        if (this == obj) return true;
        // Null or not the same class
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;
        // Compare fields for logical equality
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}

