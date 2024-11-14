package com.bankingsystem.persistence;

import com.bankingsystem.model.Bank;
import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.model.User;
import com.bankingsystem.exception.UserNotFoundException;
import com.bankingsystem.service.UserService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private BankPersistenceService bankPersistenceService;

    @InjectMocks
    private UserService userService;

//    Test Create User
    @Test
    void testCreateUser() {
        // Arrange
        Bank bank = TestDataFactory.createBank("My Bank", "MB001");
        PersonDetails person = TestDataFactory.createPerson("Jane", "Doe", "jd@gmail.com");

        // Act
        User createdUser = userService.createUser(bank, person);

        // Assert
        assertNotNull(createdUser);
        assertEquals("Jane", createdUser.getPerson().getFirstName());
        verify(userPersistenceService, times(1)).save(createdUser);
        verify(bankPersistenceService, times(1)).updateBank(bank);
        assertTrue(bank.getUsers().contains(createdUser));
    }

    @Test
    void testCreateUser_NullPerson() {
        // Arrange
        Bank bank = TestDataFactory.createBank("My Bank", "MB001");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(bank, null));
    }

    @Test
    void testCreateUser_NullBank() {
        // Arrange
        PersonDetails person = TestDataFactory.createPerson("Jane", "Doe", "jd@gmail.com");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null, person));
    }

//    Test Get User By ID
    @Test
    void testGetUserById() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        int userId = user.getUserId();
        when(userPersistenceService.getUserById(userId)).thenReturn(user);

        // Act
        User retrievedUser = userService.getUserById(userId);

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getUserId());
        verify(userPersistenceService, times(1)).getUserById(userId);
    }

    @Test
    void testGetUserById_NonExistent() {
        // Arrange
        int invalidUserId = 123;
        when(userPersistenceService.getUserById(invalidUserId)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(invalidUserId));
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        int userId = 123;
        when(userPersistenceService.getUserById(userId)).thenReturn(null);

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found for ID: 123", exception.getMessage());
        verify(userPersistenceService, times(1)).getUserById(userId);
    }

//    Test Get All Users
    @Test
    void testGetAllUsers() {
        // Arrange
        Bank bank = TestDataFactory.createBank("My Bank", "MB001");
        PersonDetails person = TestDataFactory.createPerson("Jane", "Doe", "jd@gmail.com");
        User user = userService.createUser(bank, person);

        List<User> users = new ArrayList<>();
        users.add(user);
        when(userPersistenceService.getAllUsers()).thenReturn(users);

        // Act
        List<User> retrievedUsers = userService.getAllUsers();

        // Assert
        assertNotNull(retrievedUsers);
        assertEquals(1, retrievedUsers.size());
        assertEquals(user, retrievedUsers.get(0));
        verify(userPersistenceService, times(1)).getAllUsers();
    }

    @Test
    void testGetAllUsers_NoUsersExist() {
        // Arrange
        when(userPersistenceService.getAllUsers()).thenReturn(new ArrayList<>());

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertNotNull(users);
        assertTrue(users.isEmpty());
        verify(userPersistenceService, times(1)).getAllUsers();
    }

//    Test Delete User
    @Test
    void testDeleteUser() {
        // Arrange
        Bank bank = TestDataFactory.createBank("My Bank", "MB001");
        PersonDetails person = TestDataFactory.createPerson("Jane", "Doe", "jd@gmail.com");
        User user = userService.createUser(bank, person);

        int userId = user.getUserId();
        when(userPersistenceService.deleteUser(userId)).thenReturn(true);

        // Act
        boolean isDeleted = userService.deleteUser(userId);

        // Assert
        assertTrue(isDeleted);
        verify(userPersistenceService, times(1)).deleteUser(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        int invalidUserId = 123;
        when(userPersistenceService.deleteUser(invalidUserId)).thenReturn(false);

        // Act
        boolean isDeleted = userService.deleteUser(invalidUserId);

        // Assert
        assertFalse(isDeleted);
        verify(userPersistenceService, times(1)).deleteUser(invalidUserId);
    }
}
