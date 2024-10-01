package com.bankingsystem.services;

import com.bankingsystem.models.Person;
import com.bankingsystem.persistence.PersonDAO;

public class PersonService {

    private PersonDAO personDAO = new PersonDAO();

    // Create new person
    public void createPerson(String firstName, String lastName, String email, String addressLine1, String addressLine2, String city, String country) {
        Person person = new Person(firstName, lastName, email, addressLine1, addressLine2, city, country);
        personDAO.createPerson(person);
    }

    // Get person by ID

    // Get all persons

    // Update person

    // Delete person
}
