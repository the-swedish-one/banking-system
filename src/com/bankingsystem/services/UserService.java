package com.bankingsystem.services;

import com.bankingsystem.models.Person;
import com.bankingsystem.models.User;
import com.bankingsystem.models.UserRole;
import com.bankingsystem.persistence.UserDAO;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    // Create new user
    public void createUser(String userId, UserRole userRole, Person person) {
        User user = new User(userId, userRole, person);
        userDAO.createUser(user);
    }

    // Get user by ID
    public User getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Delete user by ID
    public boolean deleteUser(String userId) {
        return userDAO.deleteUser(userId);
    }
}
