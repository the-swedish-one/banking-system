package com.bankingsystem.services;

import com.bankingsystem.models.Bank;
import com.bankingsystem.models.Person;
import com.bankingsystem.models.User;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.persistence.UserPersistenceService;

import java.util.List;

public class UserService {
    private final UserPersistenceService userPersistenceService;
    private final BankPersistenceService bankPersistenceService;

    public UserService(UserPersistenceService userPersistenceService, BankPersistenceService bankPersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.bankPersistenceService = bankPersistenceService;
    }

    // Create new user
    public User createUser(Bank bank, Person person) {
        User user = new User(person);
        userPersistenceService.createUser(user);
        bank.getUsers().add(user);
        bankPersistenceService.updateBank(bank);
        return user;
    }

    // Get user by ID
    public User getUserById(String userId) {
        return userPersistenceService.getUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userPersistenceService.getAllUsers();
    }

    // Delete user by ID
    public boolean deleteUser(String userId) {
        return userPersistenceService.deleteUser(userId);
    }
}
