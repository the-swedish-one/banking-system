package com.bankingsystem.services;

import com.bankingsystem.models.Person;
import com.bankingsystem.models.exceptions.PersonNotFoundException;
import com.bankingsystem.persistence.PersonPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        Person createdPerson = personService.createPerson("John", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");

        // Assert
        assertNotNull(createdPerson);
        assertEquals("John", createdPerson.getFirstName());

        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personPersistenceService, times(1)).createPerson(personCaptor.capture());

        // Check the captured person details
        assertEquals("John", personCaptor.getValue().getFirstName());
        assertEquals("Doe", personCaptor.getValue().getLastName());
    }

//    Test Get Person By Id
    @Test
    void testGetPersonById() {
        // Arrange
        Person person = TestDataFactory.createPerson("John", "Doe", "jd@gmail.com");
        String personId = person.getPersonId();
        when(personPersistenceService.getPersonById(personId)).thenReturn(person);

        // Act
        Person result = personService.getPersonById(personId);

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
    void testGetPersonById_NonExistent() {
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
        Person person1 = TestDataFactory.createPerson("John", "Doe", "jd@gmail.com");
        Person person2 = TestDataFactory.createPerson("Jane", "Doe", "jd2@gmail.com");
        when(personPersistenceService.getAllPersons()).thenReturn(List.of(person1, person2));

        // Act
        List<Person> result = personService.getAllPersons();

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

//    Test Delete Person
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
