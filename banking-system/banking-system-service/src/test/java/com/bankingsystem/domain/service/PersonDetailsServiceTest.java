package com.bankingsystem.domain.service;

import com.bankingsystem.domain.model.PersonDetails;
import com.bankingsystem.persistence.exception.PersonDetailsNotFoundException;
import com.bankingsystem.domain.persistence.PersonDetailsPersistenceService;
import com.bankingsystem.domain.testutils.TestDataFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonDetailsServiceTest {

    @Mock
    private PersonDetailsPersistenceService personDetailsPersistenceService;

    @InjectMocks
    private PersonDetailsService personDetailsService;

    @Test
    void createPersonDetails() {
        // Arrange
        PersonDetails personDetails = TestDataFactory.createPerson();
        when(personDetailsPersistenceService.save(personDetails)).thenReturn(personDetails);

        // Act
        PersonDetails createdPersonDetails = personDetailsService.createPersonDetails(personDetails);

        // Assert
        assertNotNull(createdPersonDetails);
        assertEquals("John", createdPersonDetails.getFirstName());
        assertEquals("Doe", createdPersonDetails.getLastName());
        assertEquals("john.doe@example.com", createdPersonDetails.getEmail());
        verify(personDetailsPersistenceService, times(1)).save(personDetails);
    }

    @Nested
    class GetPersonDetailsTests {

        @Test
        void getPersonDetailsById() {
            // Arrange
            PersonDetails personDetails = TestDataFactory.createPerson();
            int personId = personDetails.getPersonId();
            when(personDetailsPersistenceService.getPersonDetailsById(personId)).thenReturn(personDetails);

            // Act
            PersonDetails result = personDetailsService.getPersonDetailsById(personId);

            // Assert
            assertNotNull(result);
            assertEquals("John", result.getFirstName());
            assertEquals("Doe", result.getLastName());
            assertEquals("john.doe@example.com", result.getEmail());
            verify(personDetailsPersistenceService, times(1)).getPersonDetailsById(personId);
        }

        @Test
        void getPersonDetailsById_NegativeId() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> personDetailsService.getPersonDetailsById(-2));
        }

        @Test
        void getPersonDetailsById_NotFound() {
            // Arrange
            int nonExistentPersonId = 123;
            when(personDetailsPersistenceService.getPersonDetailsById(nonExistentPersonId)).thenThrow(PersonDetailsNotFoundException.class);

            // Act & Assert
            assertThrows(PersonDetailsNotFoundException.class, () -> personDetailsService.getPersonDetailsById(nonExistentPersonId));

            verify(personDetailsPersistenceService, times(1)).getPersonDetailsById(nonExistentPersonId);
        }
    }

    @Nested
    class GetAllPersonDetailsTests {

        @Test
        void getAllPersonDetails() {
            // Arrange
            PersonDetails person1 = TestDataFactory.createPerson();
            PersonDetails person2 = TestDataFactory.createPerson("Jane", "Doe");
            when(personDetailsPersistenceService.getAllPersonDetails()).thenReturn(List.of(person1, person2));

            // Act
            List<PersonDetails> result = personDetailsService.getAllPersonsDetails();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(person1.getPersonId(), result.get(0).getPersonId());
            assertEquals(person2.getPersonId(), result.get(1).getPersonId());

            verify(personDetailsPersistenceService, times(1)).getAllPersonDetails();
        }

        @Test
        void getAllPersonDetails_Empty() {
            // Arrange
            when(personDetailsPersistenceService.getAllPersonDetails()).thenThrow(PersonDetailsNotFoundException.class);

            // Act & Assert
            assertThrows(PersonDetailsNotFoundException.class, () -> personDetailsService.getAllPersonsDetails());
        }
    }

    @Nested
    class DeletePersonDetailsTests {
        @Test
        void deletePersonDetails() {
            // Arrange
            int personId = 123;
            when(personDetailsPersistenceService.deletePersonDetails(personId)).thenReturn(true);

            // Act
            boolean result = personDetailsService.deletePersonDetails(personId);

            // Assert
            assertTrue(result);
            verify(personDetailsPersistenceService, times(1)).deletePersonDetails(personId);
        }

        @Test
        void deletePersonDetails_NegativeId() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> personDetailsService.deletePersonDetails(-2));
            verify(personDetailsPersistenceService, never()).deletePersonDetails(anyInt());
        }

        @Test
        void deletePersonDetails_NotFound() {
            // Arrange
            int nonExistentPersonId = 456;
            when(personDetailsPersistenceService.deletePersonDetails(nonExistentPersonId)).thenReturn(false);

            // Act
            boolean result = personDetailsService.deletePersonDetails(nonExistentPersonId);

            // Assert
            assertFalse(result);
            verify(personDetailsPersistenceService, times(1)).deletePersonDetails(nonExistentPersonId);
        }
    }

}
