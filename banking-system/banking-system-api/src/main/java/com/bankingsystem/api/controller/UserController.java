package com.bankingsystem.api.controller;

import com.bankingsystem.api.mapper.ApiUserMapper;
import com.bankingsystem.api.model.ApiUser;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ApiUserMapper apiUserMapper;

    @Autowired
    public UserController(UserService userService, ApiUserMapper apiUserMapper) {
        this.userService = userService;
        this.apiUserMapper = apiUserMapper;
    }

    // Create a new user
    @PostMapping("/create")
    public ResponseEntity<ApiUser> createUser(@RequestBody ApiUser apiUser) {
        User user = apiUserMapper.toServiceModel(apiUser);
        User createdUser = userService.createUser(user);
        ApiUser createdApiUser = apiUserMapper.toApiModel(createdUser);
        return new ResponseEntity<>(createdApiUser, HttpStatus.CREATED);
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<ApiUser> getUserById(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        ApiUser apiUser = apiUserMapper.toApiModel(user);
        return new ResponseEntity<>(apiUser, HttpStatus.OK);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<ApiUser>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<ApiUser> apiUserList = userList.stream()
                .map(apiUserMapper::toApiModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(apiUserList, HttpStatus.OK);
    }

    // Update user
    @PutMapping("/{userId}")
    public ResponseEntity<ApiUser> updateUser(@PathVariable int userId, @RequestBody ApiUser apiUser) {
        // Ensure the ID in the path matches the ID in the payload if needed
        apiUser.setUserId(userId);
        User user = apiUserMapper.toServiceModel(apiUser);
        User updatedUser = userService.updateUser(user);
        ApiUser updatedApiUser = apiUserMapper.toApiModel(updatedUser);
        return new ResponseEntity<>(updatedApiUser, HttpStatus.OK);
    }

    // Delete user by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        boolean isDeleted = userService.deleteUser(userId);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
