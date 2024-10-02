package com.bankingsystem.services;

import com.bankingsystem.models.Person;
import com.bankingsystem.models.User;
import com.bankingsystem.persistence.UserPersistenceService;

import java.util.List;

public class UserService {
    private final UserPersistenceService userPersistenceService;

    public UserService(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

    // Create new user
    public void createUser(Person person) {
        User user = new User(person);
        userPersistenceService.createUser(user);
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
