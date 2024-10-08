package com.bankingsystem.persistence;

import com.bankingsystem.models.User;

import java.util.List;

public interface UserPersistenceService {

    /**
     * Create a new user
     */
    void createUser(User user);

    /**
     * Get a user by ID
     */
    User getUserById(String userId);

    // Get all users
    List<User> getAllUsers();

    // Update user
    void updateUser(User user);

    // Delete a user by ID
    boolean deleteUser(String userId);
}
