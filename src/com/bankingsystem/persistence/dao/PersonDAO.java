package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    private List<Person> persons = new ArrayList<>();

    // Create a new Person
    public void createPerson(Person person) {
        persons.add(person);
    }

    // Get a person by ID
    // TODO - add method here when Person has ID variable

    // Get all Persons
    public List<Person> getAllPersons() {
        return new ArrayList<Person>(persons);
    }

    // Update person
    // TODO - add method here when Person has ID variable

    // Delete person
    // TODO - add method here when Person has ID variable
}
