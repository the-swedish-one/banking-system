package com.bankingsystem.domain.service;

import com.bankingsystem.domain.model.PersonDetails;
import com.bankingsystem.domain.persistence.PersonDetailsPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.UserPersistenceService;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserPersistenceService userPersistenceService;
    private final PersonDetailsPersistenceService personDetailsPersistenceService;

    public UserService(UserPersistenceService userPersistenceService, PersonDetailsPersistenceService personDetailsPersistenceService) {
        this.userPersistenceService = userPersistenceService;
        this.personDetailsPersistenceService = personDetailsPersistenceService;
    }

    // Create new user
    public User createUser(User user) {
        logger.info("Creating new user");
        if (user == null || user.getPerson() == null) {
            logger.error("Invalid user or person data");
            throw new IllegalArgumentException("User or person data cannot be null");
        }
        PersonDetails person = user.getPerson();
        if (person.getPersonId() != null) {
            logger.info("Person ID found, fetching person details by ID: {}", person.getPersonId());
            person = personDetailsPersistenceService.getPersonDetailsById(person.getPersonId());
        } else {
            // Check for duplicate email and save new PersonDetails via Persistence Service
            if (personDetailsPersistenceService.existsByEmail(person.getEmail())) {
                logger.error("PersonDetails with email {} already exists", person.getEmail());
                throw new IllegalArgumentException("PersonDetails with email " + person.getEmail() + " already exists.");
            }
            logger.info("Saving new person details");
            person = personDetailsPersistenceService.save(person);
        }
        user.setPerson(person);
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
