package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.PersonDetails;
import com.bankingsystem.persistence.PersonPersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonDAO implements PersonPersistenceService {

    private List<PersonDetails> persons = new ArrayList<>();

    // Create a new PersonDetails
    @Override
    public void createPerson(PersonDetails person) {
        persons.add(person);
    }

    // Get a person by ID
    public PersonDetails getPersonById(String personId) {
        return persons.stream()
                .filter(person -> person.getPersonId().equals(personId))
                .findFirst()
                .orElse(null);
    }

    // Get all Persons
    @Override
    public List<PersonDetails> getAllPersons() {
        return new ArrayList<PersonDetails>(persons);
    }

    // Update person
    @Override
    public void updatePerson(PersonDetails person) {
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
