package com.bankingsystem;

import com.bankingsystem.models.Bank;
import com.bankingsystem.models.Person;
import com.bankingsystem.models.User;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.persistence.UserPersistenceService;
import com.bankingsystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private BankPersistenceService bankPersistenceService;

    @InjectMocks
    private UserService userService;

    private Bank bank;
    private Person person;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bank = new Bank("My Bank", "MB001");
        person = new Person("Jane", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");
        user = new User(person);
    }

    @Test
    void testCreateUser() {
        User createdUser = userService.createUser(bank, person);

        // Assert
        assertNotNull(createdUser);
        assertEquals("Jane", createdUser.getPerson().getFirstName());
        verify(userPersistenceService, times(1)).createUser(any(User.class));
        verify(bankPersistenceService, times(1)).updateBank(bank);
        assertTrue(bank.getUsers().contains(createdUser)); // Ensure the user is added to the bank's user list
    }

    @Test
    void testGetUserById() {
        // Arrange
        String userId = user.getUserId();
        when(userPersistenceService.getUserById(userId)).thenReturn(user);

        // Act
        User retrievedUser = userService.getUserById(userId);

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getUserId());
        verify(userPersistenceService, times(1)).getUserById(userId);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userPersistenceService.getAllUsers()).thenReturn(users);

        // Act
        List<User> retrievedUsers = userService.getAllUsers();

        // Assert
        assertNotNull(retrievedUsers);
        assertEquals(1, retrievedUsers.size());
        assertEquals(user, retrievedUsers.getFirst());
        verify(userPersistenceService, times(1)).getAllUsers();
    }

    @Test
    void testDeleteUser() {
        // Arrange
        String userId = user.getUserId();
        when(userPersistenceService.deleteUser(userId)).thenReturn(true);

        // Act
        boolean isDeleted = userService.deleteUser(userId);

        // Assert
        assertTrue(isDeleted);
        verify(userPersistenceService, times(1)).deleteUser(userId);
    }
}
