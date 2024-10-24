package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.PersonDetails;
import com.bankingsystem.persistence.PersonDetailsPersistenceService;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailsDAO implements PersonDetailsPersistenceService {

    private List<PersonDetails> personDetails = new ArrayList<>();

    // Create new PersonDetails
    @Override
    public PersonDetails save(PersonDetails personDetails) {
        this.personDetails.add(personDetails);
        return personDetails;
    }

    // Get a person details by ID
    public PersonDetails getPersonDetailsById(String personDetailsId) {
        return personDetails.stream()
                .filter(person -> person.getPersonId().equals(personDetailsId))
                .findFirst()
                .orElse(null);
    }

    // Get all Person Details
    @Override
    public List<PersonDetails> getAllPersonDetails() {
        return new ArrayList<PersonDetails>(personDetails);
    }

    // Update person details
    @Override
    public void updatePersonDetails(PersonDetails person) {
        personDetails.stream()
                .filter(p -> p.getPersonId().equals(person.getPersonId()))
                .findFirst()
                .ifPresent(p -> p = person);
    }

    // Delete person
    @Override
    public boolean deletePersonDetails(String personDetailsId) {
        return personDetails.removeIf(person -> person.getPersonId().equals(personDetailsId));
    }
}
