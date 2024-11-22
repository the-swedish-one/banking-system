package com.bankingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bankingsystem.model.User;
import com.bankingsystem.exception.UserNotFoundException;
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
        User savedUser = userPersistenceService.save(user);
        logger.info("Successfully created user with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    // Get user by ID
    public User getUserById(int userId) {
        logger.info("Fetching user by ID: {}", userId);
        if (userId <= 0) {
            logger.error("Invalid user ID: {}", userId);
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        User user = userPersistenceService.getUserById(userId);
        logger.info("Successfully fetched user for ID: {}", userId);
        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> usersList = userPersistenceService.getAllUsers();
        logger.info("Successfully fetched {} users", usersList.size());
        return usersList;
    }

    // Update user
    public User updateUser(User user) {
        logger.info("Updating user with ID: {}", user.getUserId());
        User updatedUser = userPersistenceService.updateUser(user);
        logger.info("Successfully updated user with ID: {}", updatedUser.getUserId());
        return updatedUser;
    }

    // Delete user by ID
    public boolean deleteUser(int userId) {
        logger.info("Deleting user by ID: {}", userId);
        if (userId <= 0) {
            logger.error("Invalid user ID: {}", userId);
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        boolean isDeleted = userPersistenceService.deleteUser(userId);
        if (isDeleted) {
            logger.info("Successfully deleted user with ID: {}", userId);
        } else {
            logger.warn("Failed to delete user with ID: {}", userId);
        }
        return isDeleted;
    }
}
