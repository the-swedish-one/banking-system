package com.bankingsystem.persistence.impl;

import com.bankingsystem.mapper.PersonDetailsMapper;
import com.bankingsystem.mapper.UserMapper;
import com.bankingsystem.model.PersonDetailsEntity;
import com.bankingsystem.model.User;
import com.bankingsystem.model.UserEntity;
import com.bankingsystem.exception.UserNotFoundException;
import com.bankingsystem.persistence.UserPersistenceService;
import com.bankingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPersistenceServiceImpl implements UserPersistenceService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PersonDetailsMapper personDetailsMapper;

    public UserPersistenceServiceImpl(UserRepository userRepository, UserMapper userMapper, PersonDetailsMapper personDetailsMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.personDetailsMapper = personDetailsMapper;
    }

    // Create or save user
    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(entity);
        return userMapper.toModel(savedEntity);
    }

    // Get a user by ID
    @Override
    public User getUserById(int userId) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toModel(entity);
    }

    // Get all users
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toModel)
                .collect(Collectors.toList());
    }

    // Update user
    @Override
    public User updateUser(User user) {
        UserEntity existingEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        PersonDetailsEntity personDetailsEntity = personDetailsMapper.toEntity(user.getPerson());
        existingEntity.setPerson(personDetailsEntity);

        UserEntity updatedEntity = userRepository.save(existingEntity);
        return userMapper.toModel(updatedEntity);
    }

    // Delete a user by ID
    @Override
    public boolean deleteUser(int userId) {
        if(!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        userRepository.deleteById(userId);
        return true;
    }
}
