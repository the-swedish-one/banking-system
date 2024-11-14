package com.bankingsystem.service;

import org.springframework.stereotype.Service;

import com.bankingsystem.model.User;
import com.bankingsystem.exception.UserNotFoundException;
import com.bankingsystem.persistence.UserPersistenceService;

import java.util.List;

@Service
public class UserService {
    private final UserPersistenceService userPersistenceService;

    public UserService(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

    // Create new user
    public User createUser(User user) {
        return userPersistenceService.save(user);
    }

    // Get user by ID
    public User getUserById(int userId) {
        return userPersistenceService.getUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> usersList = userPersistenceService.getAllUsers();
        if (usersList.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return usersList;
    }

    // Update user
    public User updateUser(User user) {
        return userPersistenceService.updateUser(user);
    }

    // Delete user by ID
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        return userPersistenceService.deleteUser(userId);
    }
}
