package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Person;
import com.bankingsystem.persistence.PersonPersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonDAO implements PersonPersistenceService {

    private List<Person> persons = new ArrayList<>();

    // Create a new Person
    @Override
    public void createPerson(Person person) {
        persons.add(person);
    }

    // Get a person by ID
    public Person getPersonById(String personId) {
        return persons.stream()
                .filter(person -> person.getPersonId().equals(personId))
                .findFirst()
                .orElse(null);
    }

    // Get all Persons
    @Override
    public List<Person> getAllPersons() {
        return new ArrayList<Person>(persons);
    }

    // Update person
    @Override
    public void updatePerson(Person person) {
        for (int i = 0; i < persons.size(); i++) {
            if (Objects.equals(persons.get(i).getPersonId(), person.getPersonId())) {
                persons.set(i, person);
                return;
            }
        }
    }

    // Delete person
    @Override
    public boolean deletePerson(String personId) {
        return persons.removeIf(person -> person.getPersonId().equals(personId));
    }
}
