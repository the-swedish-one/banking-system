package com.bankingsystem.domain.persistence;

import com.bankingsystem.domain.model.PersonDetails;

import java.util.List;

public interface PersonDetailsPersistenceService {

    // Create a new PersonDetails
    PersonDetails save(PersonDetails personDetails);

    // Get a person by ID
    PersonDetails getPersonDetailsById(int personDetailsId);

    // Get all Persons
    List<PersonDetails> getAllPersonDetails();

    // Update person
    PersonDetails updatePersonDetails(PersonDetails personDetails);

    // Delete person
    boolean deletePersonDetails(int personDetailsId);
}
