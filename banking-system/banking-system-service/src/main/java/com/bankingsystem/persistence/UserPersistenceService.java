package com.bankingsystem.persistence;

import com.bankingsystem.model.UserEntity;

import java.util.List;

public interface UserPersistenceService {

    /**
     * Create a new user
     */
    UserEntity save(UserEntity user);

    /**
     * Get a user by ID
     */
    UserEntity getUserById(int userId);

    // Get all users
    List<UserEntity> getAllUsers();

    // Update user
    void updateUser(UserEntity user);

    // Delete a user by ID
    boolean deleteUser(int userId);
}
