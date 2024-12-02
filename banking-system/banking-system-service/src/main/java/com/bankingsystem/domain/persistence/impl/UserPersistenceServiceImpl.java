package com.bankingsystem.domain.persistence.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bankingsystem.domain.mapper.PersonDetailsMapper;
import com.bankingsystem.domain.mapper.UserMapper;
import com.bankingsystem.persistence.model.PersonDetailsEntity;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.persistence.model.UserEntity;
import com.bankingsystem.persistence.exception.UserNotFoundException;
import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPersistenceServiceImpl implements UserPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(UserPersistenceServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PersonDetailsMapper personDetailsMapper;

    @Autowired
    public UserPersistenceServiceImpl(UserRepository userRepository, UserMapper userMapper, PersonDetailsMapper personDetailsMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.personDetailsMapper = personDetailsMapper;
    }

    // Create or save user
    @Override
    public User save(User user) {
        logger.info("Saving new user");
        if (user == null) {
            logger.error("User object is null");
            throw new IllegalArgumentException("User object cannot be null");
        }
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(entity);
        logger.info("Successfully saved user with ID: {}", savedEntity.getUserId());
        return userMapper.toModel(savedEntity);
    }

    // Get a user by ID
    @Override
    public User getUserById(int userId) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found for ID: {}", userId);
                    return new UserNotFoundException("User not found");
                });
        logger.info("Successfully fetched user with ID: {}", userId);
        return userMapper.toModel(entity);
    }

    // Get all users
    @Override
    public List<User> getAllUsers() {
        List<UserEntity> entities = userRepository.findAll();
        if (entities.isEmpty()) {
            logger.warn("No users found in the database");
            throw new UserNotFoundException("No users found");
        }
        List<User> users = entities.stream()
                .map(userMapper::toModel)
                .collect(Collectors.toList());
        logger.info("Successfully fetched {} users", users.size());
        return users;
    }

    // Update user
    @Override
    public User updateUser(User user) {
        UserEntity existingEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found for ID: {}", user.getUserId());
                    return new UserNotFoundException("User not found");
                });

        PersonDetailsEntity personDetailsEntity = personDetailsMapper.toEntity(user.getPerson());
        existingEntity.setPerson(personDetailsEntity);

        UserEntity updatedEntity = userRepository.save(existingEntity);
        logger.info("Successfully updated user with ID: {}", updatedEntity.getUserId());
        return userMapper.toModel(updatedEntity);
    }

    // Delete a user by ID
    @Override
    public boolean deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            logger.error("User not found for ID: {}", userId);
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
        return true;
    }
}
