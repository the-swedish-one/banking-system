package com.bankingsystem.service;

import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.exception.PersonDetailsNotFoundException;
import com.bankingsystem.persistence.PersonDetailsPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonDetailsService {

    private final PersonDetailsPersistenceService personDetailsPersistenceService;

    public PersonDetailsService(PersonDetailsPersistenceService personDetailsPersistenceService) {
        this.personDetailsPersistenceService = personDetailsPersistenceService;
    }

    // Create new person
    public PersonDetails createPersonDetails(PersonDetails personDetails) {
        return personDetailsPersistenceService.save(personDetails);
    }

    // Get person by ID
    public PersonDetails getPersonDetailsById(int personDetailsId) {
        if (personDetailsId <= 0) {
            throw new IllegalArgumentException("PersonDetails ID must be greater than zero");
        }
        return personDetailsPersistenceService.getPersonDetailsById(personDetailsId);
    }

    // Get all persons
    public List<PersonDetails> getAllPersonsDetails() {
        List<PersonDetails> personDetailsList = personDetailsPersistenceService.getAllPersonDetails();
        if (personDetailsList.isEmpty()) {
            throw new PersonDetailsNotFoundException("No person details found");
        }
        return personDetailsList;
    }

    // Update person details
    public PersonDetails updatePersonDetails(PersonDetails personDetails) {
        return personDetailsPersistenceService.updatePersonDetails(personDetails);
    }

    // Delete person by ID
    public boolean deletePersonDetails(int personDetailsId) {
        if (personDetailsId <= 0) {
            throw new IllegalArgumentException("PersonDetails ID must be greater than zero");
        }
        return personDetailsPersistenceService.deletePersonDetails(personDetailsId);
    }
}
