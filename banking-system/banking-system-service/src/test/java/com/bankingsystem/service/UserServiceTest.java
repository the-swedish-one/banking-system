package com.bankingsystem.service;

import com.bankingsystem.model.Bank;
import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.model.User;
import com.bankingsystem.exception.UserNotFoundException;
import com.bankingsystem.persistence.UserPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Nested;
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

    @InjectMocks
    private UserService userService;

    @Nested
    class CreateUserTests {

        @Test
        void testCreateUser() {
            // Arrange
            User u = TestDataFactory.createUser();

            // Act
            User user = userService.createUser(u);

            // Assert
            assertNotNull(user);
            assertEquals("John", user.getPerson().getFirstName());
            verify(userPersistenceService, times(1)).save(user);
        }

        @Test
        void testCreateUser_NullPerson() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
        }
    }

    @Nested
    class GetUserByIdTests {
        @Test
        void testGetUserById() {
            // Arrange
            User user = TestDataFactory.createUser();
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
    }

    @Nested
    class GetAllUsersTests {
        @Test
        void testGetAllUsers() {
            // Arrange
            User u = TestDataFactory.createUser();
            User user = userService.createUser(u);

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
    }

    @Nested
    class DeleteUserTests {
        @Test
        void testDeleteUser() {
            // Arrange
            User u = TestDataFactory.createUser();
            User user = userService.createUser(u);

            int userId = user.getUserId();
            when(userPersistenceService.deleteUser(userId)).thenReturn(true);

            // Act
            boolean isDeleted = userService.deleteUser(userId);

            // Assert
            assertTrue(isDeleted);
            verify(userPersistenceService, times(1)).deleteUser(userId);
            assertNull(userPersistenceService.getUserById(userId), "User should be deleted and no longer present in the repository");
        }

        @Test
        void testDeleteUser_NotFound() {
            // Arrange
            int invalidUserId = 123;
            when(userPersistenceService.deleteUser(invalidUserId)).thenReturn(false);
            when(userPersistenceService.getUserById(invalidUserId)).thenReturn(null);

            // Act
            assertThrows(UserNotFoundException.class, () -> {userService.deleteUser(invalidUserId);});
            verify(userPersistenceService, times(1)).deleteUser(invalidUserId);
        }
    }
}
