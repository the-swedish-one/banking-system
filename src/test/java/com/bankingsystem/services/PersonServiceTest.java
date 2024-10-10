package com.bankingsystem.services;

import com.bankingsystem.models.PersonDetails;
import com.bankingsystem.models.exceptions.PersonNotFoundException;
import com.bankingsystem.persistence.PersonPersistenceService;
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
public class PersonServiceTest {

    @Mock
    private PersonPersistenceService personPersistenceService;

    @InjectMocks
    private PersonService personService;

    @Test
    void testCreatePerson() {
        // Act
        PersonDetails createdPerson = personService.createPerson("John", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");

        // Assert
        assertNotNull(createdPerson);
        assertEquals("John", createdPerson.getFirstName());
        verify(personPersistenceService, times(1)).createPerson(createdPerson);
    }

//    Test Get PersonDetails By ID
    @Test
    void testGetPersonById() {
        // Arrange
        PersonDetails person = TestDataFactory.createPerson("John", "Doe", "jd@gmail.com");
        String personId = person.getPersonId();
        when(personPersistenceService.getPersonById(personId)).thenReturn(person);

        // Act
        PersonDetails result = personService.getPersonById(personId);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(personPersistenceService, times(1)).getPersonById(personId);
    }

    @Test
    void testGetPersonById_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> personService.getPersonById(null));
    }

    @Test
    void testGetPersonById_EmptyId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> personService.getPersonById(""));

        verify(personPersistenceService, never()).getPersonById(anyString());
    }

    @Test
    void testGetPersonById_NotFound() {
        // Arrange
        String invalidPersonId = "nonexistent-person-id";
        when(personPersistenceService.getPersonById(invalidPersonId)).thenReturn(null);

        // Act & Assert
        assertThrows(PersonNotFoundException.class, () -> personService.getPersonById(invalidPersonId));

        verify(personPersistenceService, times(1)).getPersonById(invalidPersonId);
    }

//    Test Get All Persons
    @Test
    void testGetAllPersons() {
        // Arrange
        PersonDetails person1 = TestDataFactory.createPerson("John", "Doe", "jd@gmail.com");
        PersonDetails person2 = TestDataFactory.createPerson("Jane", "Doe", "jd2@gmail.com");
        when(personPersistenceService.getAllPersons()).thenReturn(List.of(person1, person2));

        // Act
        List<PersonDetails> result = personService.getAllPersons();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(person1.getPersonId(), result.get(0).getPersonId());
        assertEquals(person2.getPersonId(), result.get(1).getPersonId());

        verify(personPersistenceService, times(1)).getAllPersons();
    }

    @Test
    void testGetAllPersons_Empty() {
        // Arrange
        when(personPersistenceService.getAllPersons()).thenReturn(List.of());

        // Act & Assert
        assertThrows(PersonNotFoundException.class, () -> personService.getAllPersons());
    }

//    Test Delete PersonDetails
    @Test
    void testDeletePerson() {
        // Arrange
        String personId = "person-id";
        when(personPersistenceService.deletePerson(personId)).thenReturn(true);

        // Act
        boolean result = personService.deletePerson(personId);

        // Assert
        assertTrue(result);
        verify(personPersistenceService, times(1)).deletePerson(personId);
    }

    @Test
    void testDeletePerson_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> personService.deletePerson(null));
        verify(personPersistenceService, never()).deletePerson(anyString());
    }

    @Test
    void testDeletePerson_NotFound() {
        // Arrange
        String nonExistentPersonId = "non-existent-id";
        when(personPersistenceService.deletePerson(nonExistentPersonId)).thenReturn(false);

        // Act
        boolean result = personService.deletePerson(nonExistentPersonId);

        // Assert
        assertFalse(result);
        verify(personPersistenceService, times(1)).deletePerson(nonExistentPersonId);
    }

}
