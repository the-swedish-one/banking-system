package com.bankingsystem.services;

import com.bankingsystem.models.PersonDetails;
import com.bankingsystem.models.exceptions.PersonNotFoundException;
import com.bankingsystem.persistence.PersonPersistenceService;

import java.util.List;

public class PersonService {

    private final PersonPersistenceService personPersistenceService;

    public PersonService(PersonPersistenceService personPersistenceService) {
        this.personPersistenceService = personPersistenceService;
    }

    // Create new person
    public PersonDetails createPerson(String firstName, String lastName, String email, String addressLine1, String addressLine2, String city, String country) {
        PersonDetails person = new PersonDetails(firstName, lastName, email, addressLine1, addressLine2, city, country);
        personPersistenceService.createPerson(person);
        return person;
    }

    public PersonDetails getPersonById(String personId) {
        if (personId == null || personId.isEmpty()) {
            throw new IllegalArgumentException("PersonDetails ID cannot be null or empty");
        }
        PersonDetails person = personPersistenceService.getPersonById(personId);
        if (person == null) {
            throw new PersonNotFoundException("PersonDetails not found for ID: " + personId);
        }
        return person;
    }


    // Get all persons
    public List<PersonDetails> getAllPersons() {
        List<PersonDetails> persons = personPersistenceService.getAllPersons();
        if (persons.isEmpty()) {
            throw new PersonNotFoundException("No persons found");
        }
        return persons;
    }

    // Delete person by ID
    public boolean deletePerson(String personId) {
        if (personId == null) {
            throw new IllegalArgumentException("PersonDetails ID cannot be null");
        }
        return personPersistenceService.deletePerson(personId);
    }
}
