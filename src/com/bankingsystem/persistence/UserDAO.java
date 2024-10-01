package com.bankingsystem.persistence;

import com.bankingsystem.models.Account;
import com.bankingsystem.models.User;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private List<User> users = new ArrayList<>();

    // Create a new user
    public void createUser(User user) {
        users.add(user);
    }

    // Get a user by ID
    public User getUserById(String userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // Get all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Update user
    // TODO - update user to database
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == user.getUserId()) {
                users.set(i, user);
                return;
            }
        }
    }

    // Delete a user by ID
    public boolean deleteUser(String userId) {
        return users.removeIf(user -> user.getUserId().equals(userId));
    }
}
