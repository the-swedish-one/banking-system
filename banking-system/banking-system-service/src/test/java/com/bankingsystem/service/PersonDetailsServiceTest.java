package com.bankingsystem.service;

import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.exception.PersonDetailsNotFoundException;
import com.bankingsystem.persistence.PersonDetailsPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
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
    void testCreatePersonDetails() {
        // Arrange
        PersonDetails personDetails = TestDataFactory.createPerson();
        when(personDetailsPersistenceService.save(personDetails)).thenReturn(personDetails);

        // Act
        PersonDetails createdPersonDetails = personDetailsService.createPersonDetails(personDetails);

        // Assert
        assertNotNull(createdPersonDetails);
        assertEquals("John", createdPersonDetails.getFirstName());
        assertEquals("Doe", createdPersonDetails.getLastName());
        assertEquals("jd@gmail.com", createdPersonDetails.getEmail());
        verify(personDetailsPersistenceService, times(1)).save(personDetails);
    }

    @Nested
    class GetPersonDetailsTests {

        @Test
        void testGetPersonDetailsById() {
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
            assertEquals("jd@gmail.com", result.getEmail());
            verify(personDetailsPersistenceService, times(1)).getPersonDetailsById(personId);
        }

        @Test
        void testGetPersonDetailsById_NegativeId() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> personDetailsService.getPersonDetailsById(-2));
        }

        @Test
        void testGetPersonDetailsById_NotFound() {
            // Arrange
            int invalidPersonId = 123;
            when(personDetailsPersistenceService.getPersonDetailsById(invalidPersonId)).thenReturn(null);

            // Act & Assert
            assertThrows(PersonDetailsNotFoundException.class, () -> personDetailsService.getPersonDetailsById(invalidPersonId));

            verify(personDetailsPersistenceService, times(1)).getPersonDetailsById(invalidPersonId);
        }
    }

    @Nested
    class GetAllPersonDetailsTests {

        @Test
        void testGetAllPersonDetails() {
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
        void testGetAllPersonDetails_Empty() {
            // Arrange
            when(personDetailsPersistenceService.getAllPersonDetails()).thenReturn(List.of());

            // Act & Assert
            assertThrows(PersonDetailsNotFoundException.class, () -> personDetailsService.getAllPersonsDetails());
        }
    }

    @Nested
    class DeletePersonDetailsTests {
        @Test
        void testDeletePersonDetails() {
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
        void testDeletePersonDetails_NegativeId() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> personDetailsService.deletePersonDetails(-2));
            verify(personDetailsPersistenceService, never()).deletePersonDetails(anyInt());
        }

        @Test
        void testDeletePersonDetails_NotFound() {
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
