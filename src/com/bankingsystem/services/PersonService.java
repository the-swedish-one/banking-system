package com.bankingsystem.services;

import com.bankingsystem.models.Person;
import com.bankingsystem.models.User;
import com.bankingsystem.persistence.PersonPersistenceService;
import com.bankingsystem.persistence.dao.PersonDAO;

import java.util.List;

public class PersonService {

    private final PersonPersistenceService personPersistenceService;

    public PersonService(PersonPersistenceService personPersistenceService) {
        this.personPersistenceService = personPersistenceService;
    }

    // Create new person
    public void createPerson(String firstName, String lastName, String email, String addressLine1, String addressLine2, String city, String country) {
        Person person = new Person(firstName, lastName, email, addressLine1, addressLine2, city, country);
        personPersistenceService.createPerson(person);
    }

    // Get person by ID
    public Person getPersonById(String personId) {
        return personPersistenceService.getPersonById(personId);
    }

    // Get all persons
    public List<Person> getAllPersons() {
        return personPersistenceService.getAllPersons();
    }

    // Delete person by ID
    public boolean deletePerson(String personId) {
        return personPersistenceService.deletePerson(personId);
    }
}
