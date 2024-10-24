package com.bankingsystem.services;

import com.bankingsystem.models.Bank;
import com.bankingsystem.models.PersonDetails;
import com.bankingsystem.models.User;
import com.bankingsystem.models.exceptions.UserNotFoundException;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.persistence.UserPersistenceService;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserPersistenceService userPersistenceService;
    private final BankPersistenceService bankPersistenceService;

    public UserService(UserPersistenceService userPersistenceService, BankPersistenceService bankPersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.bankPersistenceService = bankPersistenceService;
    }

    // Create new user
    public User createUser(Bank bank, PersonDetails person) {
        if (bank == null || person == null) {
            throw new IllegalArgumentException("Bank and PersonDetails cannot be null");
        }
        User user = new User(person);
        userPersistenceService.save(user);
        bank.getUsers().add(user);
        bankPersistenceService.updateBank(bank);
        return user;
    }

    // Get user by ID
    public User getUserById(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        User user = userPersistenceService.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found for ID: " + userId);
        }
        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = userPersistenceService.getAllUsers();
        return (users == null) ? new ArrayList<>() : users;
    }

    // Delete user by ID
    public boolean deleteUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return userPersistenceService.deleteUser(userId);
    }
}
