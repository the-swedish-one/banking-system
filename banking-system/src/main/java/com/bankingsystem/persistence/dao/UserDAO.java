package com.bankingsystem.persistence.dao;

import com.bankingsystem.model.User;
import com.bankingsystem.persistence.UserPersistenceService;

import java.util.ArrayList;
import java.util.List;

public class UserDAO implements UserPersistenceService {

    private List<User> users = new ArrayList<>();

    // Create a new user
    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }

    // Get a user by ID
    @Override
    public User getUserById(String userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // Get all users
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Update user
    @Override
    public void updateUser(User user) {
        users.stream()
                .filter(u -> u.getUserId().equals(user.getUserId()))
                .findFirst()
                .ifPresent(u -> u = user);
    }

    // Delete a user by ID
    @Override
    public boolean deleteUser(String userId) {
        return users.removeIf(user -> user.getUserId().equals(userId));
    }
}
