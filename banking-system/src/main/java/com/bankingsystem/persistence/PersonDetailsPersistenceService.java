package com.bankingsystem.persistence;

import com.bankingsystem.model.PersonDetails;

import java.util.List;

public interface PersonDetailsPersistenceService {

    // Create a new PersonDetails
    PersonDetails save(PersonDetails person);

    // Get a person by ID
    PersonDetails getPersonDetailsById(String personDetailsId);

    // Get all Persons
    List<PersonDetails> getAllPersonDetails();

    // Update person
    void updatePersonDetails(PersonDetails personDetails);

    // Delete person
    boolean deletePersonDetails(String personDetailsId);
}
