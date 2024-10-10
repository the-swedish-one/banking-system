package com.bankingsystem.persistence;

import com.bankingsystem.models.PersonDetails;

import java.util.List;

public interface PersonPersistenceService {

    // Create a new PersonDetails
    void createPerson(PersonDetails person);

    // Get a person by ID
    PersonDetails getPersonById(String personId);

    // Get all Persons
    List<PersonDetails> getAllPersons();

    // Update person
    void updatePerson(PersonDetails person);

    // Delete person
    boolean deletePerson(String personId);
}
