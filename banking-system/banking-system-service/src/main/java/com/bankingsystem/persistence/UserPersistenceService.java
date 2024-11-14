package com.bankingsystem.persistence;

import com.bankingsystem.model.User;

import java.util.List;

public interface UserPersistenceService {

    /**
     * Create a new user
     */
    User save(User user);

    /**
     * Get a user by ID
     */
    User getUserById(int userId);

    // Get all users
    List<User> getAllUsers();

    // Update user
    User updateUser(User user);

    // Delete a user by ID
    boolean deleteUser(int userId);
}
