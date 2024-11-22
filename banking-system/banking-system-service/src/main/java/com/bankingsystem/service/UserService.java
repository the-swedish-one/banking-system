package com.bankingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bankingsystem.model.User;
import com.bankingsystem.persistence.UserPersistenceService;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserPersistenceService userPersistenceService;

    public UserService(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

    // Create new user
    public User createUser(User user) {
        logger.info("Creating new user");
        return userPersistenceService.save(user);
    }

    // Get user by ID
    public User getUserById(int userId) {
        logger.info("Fetching user by ID: {}", userId);
        if (userId <= 0) {
            logger.error("Invalid user ID: {}", userId);
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        return userPersistenceService.getUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userPersistenceService.getAllUsers();
    }

    // Update user
    public User updateUser(User user) {
        logger.info("Updating user with ID: {}", user.getUserId());
        return userPersistenceService.updateUser(user);
    }

    // Delete user by ID
    public boolean deleteUser(int userId) {
        logger.info("Deleting user by ID: {}", userId);
        if (userId <= 0) {
            logger.error("Invalid user ID: {}", userId);
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        try {
            boolean isDeleted = userPersistenceService.deleteUser(userId);
            logger.info("Successfully deleted user with ID: {}", userId);
            return isDeleted;
        } catch (Exception ex) {
            logger.error("Failed to delete user with ID: {}", userId);
            throw ex;
        }
    }
}
