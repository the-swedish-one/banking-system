package com.bankingsystem.service;

import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.exception.PersonDetailsNotFoundException;
import com.bankingsystem.persistence.PersonDetailsPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
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
        // Act
        PersonDetails createdPersonDetails = personDetailsService.createPersonDetails("John", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");

        // Assert
        assertNotNull(createdPersonDetails);
        assertEquals("John", createdPersonDetails.getFirstName());
        verify(personDetailsPersistenceService, times(1)).save(createdPersonDetails);
    }

//    Test Get PersonDetails By ID
    @Test
    void testGetPersonDetailsById() {
        // Arrange
        PersonDetails personDetails = TestDataFactory.createPerson("John", "Doe", "jd@gmail.com");
        String personId = personDetails.getPersonId();
        when(personDetailsPersistenceService.getPersonDetailsById(personId)).thenReturn(personDetails);

        // Act
        PersonDetails result = personDetailsService.getPersonDetailsById(personId);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(personDetailsPersistenceService, times(1)).getPersonDetailsById(personId);
    }

    @Test
    void testGetPersonDetailsById_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> personDetailsService.getPersonDetailsById(null));
    }

    @Test
    void testGetPersonDetailsById_EmptyId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> personDetailsService.getPersonDetailsById(""));

        verify(personDetailsPersistenceService, never()).getPersonDetailsById(anyString());
    }

    @Test
    void testGetPersonDetailsById_NotFound() {
        // Arrange
        String invalidPersonId = "nonexistent-person-id";
        when(personDetailsPersistenceService.getPersonDetailsById(invalidPersonId)).thenReturn(null);

        // Act & Assert
        assertThrows(PersonDetailsNotFoundException.class, () -> personDetailsService.getPersonDetailsById(invalidPersonId));

        verify(personDetailsPersistenceService, times(1)).getPersonDetailsById(invalidPersonId);
    }

//    Test Get All Persons
    @Test
    void testGetAllPersonDetails() {
        // Arrange
        PersonDetails person1 = TestDataFactory.createPerson("John", "Doe", "jd@gmail.com");
        PersonDetails person2 = TestDataFactory.createPerson("Jane", "Doe", "jd2@gmail.com");
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
    void testGetAllPersonsDetails_Empty() {
        // Arrange
        when(personDetailsPersistenceService.getAllPersonDetails()).thenReturn(List.of());

        // Act & Assert
        assertThrows(PersonDetailsNotFoundException.class, () -> personDetailsService.getAllPersonsDetails());
    }

//    Test Delete PersonDetails
    @Test
    void testDeletePersonDetails() {
        // Arrange
        String personId = "person-id";
        when(personDetailsPersistenceService.deletePersonDetails(personId)).thenReturn(true);

        // Act
        boolean result = personDetailsService.deletePersonDetails(personId);

        // Assert
        assertTrue(result);
        verify(personDetailsPersistenceService, times(1)).deletePersonDetails(personId);
    }

    @Test
    void testDeletePersonDetails_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> personDetailsService.deletePersonDetails(null));
        verify(personDetailsPersistenceService, never()).deletePersonDetails(anyString());
    }

    @Test
    void testDeletePersonDetails_NotFound() {
        // Arrange
        String nonExistentPersonId = "non-existent-id";
        when(personDetailsPersistenceService.deletePersonDetails(nonExistentPersonId)).thenReturn(false);

        // Act
        boolean result = personDetailsService.deletePersonDetails(nonExistentPersonId);

        // Assert
        assertFalse(result);
        verify(personDetailsPersistenceService, times(1)).deletePersonDetails(nonExistentPersonId);
    }

}
