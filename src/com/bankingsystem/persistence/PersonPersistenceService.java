package com.bankingsystem.persistence;

import com.bankingsystem.models.Person;

import java.util.List;

public interface PersonPersistenceService {

    // Create a new Person
    void createPerson(Person person);

    // Get a person by ID
    Person getPersonById(String personId);

    // Get all Persons
    List<Person> getAllPersons();

    // Update person
    void updatePerson(Person person);

    // Delete person
    boolean deletePerson(String personId);
}
