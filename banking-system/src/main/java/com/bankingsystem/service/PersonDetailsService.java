package com.bankingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.exception.PersonDetailsNotFoundException;
import com.bankingsystem.persistence.PersonDetailsPersistenceService;

import java.util.List;

@Service
@Transactional
public class PersonDetailsService {

    private final PersonDetailsPersistenceService personDetailsPersistenceService;

    @Autowired
    public PersonDetailsService(PersonDetailsPersistenceService personPersistenceService) {
        this.personDetailsPersistenceService = personPersistenceService;
    }

    // Create new person
    public PersonDetails createPersonDetails(String firstName, String lastName, String email, String addressLine1, String addressLine2, String city, String country) {
        PersonDetails person = new PersonDetails(firstName, lastName, email, addressLine1, addressLine2, city, country);
        personDetailsPersistenceService.save(person);
        return person;
    }

    public PersonDetails getPersonDetailsById(String personDetailsId) {
        if (personDetailsId == null || personDetailsId.isEmpty()) {
            throw new IllegalArgumentException("PersonDetails ID cannot be null or empty");
        }
        PersonDetails personDetails = personDetailsPersistenceService.getPersonDetailsById(personDetailsId);
        if (personDetails == null) {
            throw new PersonDetailsNotFoundException("PersonDetails not found for ID: " + personDetailsId);
        }
        return personDetails;
    }


    // Get all persons
    public List<PersonDetails> getAllPersonsDetails() {
        List<PersonDetails> personDetails = personDetailsPersistenceService.getAllPersonDetails();
        if (personDetails.isEmpty()) {
            throw new PersonDetailsNotFoundException("No person details found");
        }
        return personDetails;
    }

    // Delete person by ID
    public boolean deletePersonDetails(String personDetailsId) {
        if (personDetailsId == null) {
            throw new IllegalArgumentException("PersonDetails ID cannot be null");
        }
        return personDetailsPersistenceService.deletePersonDetails(personDetailsId);
    }
}
