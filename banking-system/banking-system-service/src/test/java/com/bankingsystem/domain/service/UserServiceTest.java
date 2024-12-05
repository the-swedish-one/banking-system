package com.bankingsystem.domain.service;

import com.bankingsystem.domain.model.PersonDetails;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.PersonDetailsPersistenceService;
import com.bankingsystem.persistence.exception.UserNotFoundException;
import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.domain.testutils.TestDataFactory;
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

    @Mock
    private PersonDetailsPersistenceService personDetailsPersistenceService;

    @InjectMocks
    private UserService userService;

    @Nested
    class CreateUserTests {

        @Test
        void createUser() {
            // Arrange
            User u = TestDataFactory.createUser();
            PersonDetails mockPerson = u.getPerson();
            int personId = mockPerson.getPersonId();
            when(personDetailsPersistenceService.getPersonDetailsById(personId))
                    .thenReturn(mockPerson);
            when(userPersistenceService.save(u)).thenReturn(u);

            // Act
            User user = userService.createUser(u);

            // Assert
            assertNotNull(user);
            assertEquals("John", user.getPerson().getFirstName());
            verify(userPersistenceService, times(1)).save(user);
        }

        @Test
        void createUser_NullPerson() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
        }
    }

    @Nested
    class GetUserByIdTests {
        @Test
        void getUserById() {
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
        void getUserById_UserNotFound() {
            // Arrange
            int userId = 123;
            when(userPersistenceService.getUserById(userId)).thenThrow(new UserNotFoundException("User not found"));

            // Act & Assert
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
                userService.getUserById(userId);
            });

            assertEquals("User not found", exception.getMessage());
            verify(userPersistenceService, times(1)).getUserById(userId);
        }
    }

    @Nested
    class GetAllUsersTests {
        @Test
        void getAllUsers() {
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
        void getAllUsers_NoUsersExist() {
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
        void deleteUser() {
            // Arrange
            User u = TestDataFactory.createUser();

            int userId = u.getUserId();
            when(userPersistenceService.deleteUser(userId)).thenReturn(true);

            // Act
            boolean isDeleted = userService.deleteUser(userId);

            // Assert
            assertTrue(isDeleted);
            verify(userPersistenceService, times(1)).deleteUser(userId);
            assertNull(userPersistenceService.getUserById(userId), "User should be deleted and no longer present in the repository");
        }

        @Test
        void deleteUser_NotFound() {
            // Arrange
            int invalidUserId = 123;
            when(userPersistenceService.deleteUser(invalidUserId))
                    .thenThrow(new UserNotFoundException("User not found with ID: " + invalidUserId));

            // Act
            assertThrows(UserNotFoundException.class, () -> {userService.deleteUser(invalidUserId);});
            verify(userPersistenceService, times(1)).deleteUser(invalidUserId);
        }
    }
}
