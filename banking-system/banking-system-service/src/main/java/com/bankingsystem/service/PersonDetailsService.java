package com.bankingsystem.service;

import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.persistence.PersonDetailsPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(PersonDetailsService.class);

    private final PersonDetailsPersistenceService personDetailsPersistenceService;

    public PersonDetailsService(PersonDetailsPersistenceService personDetailsPersistenceService) {
        this.personDetailsPersistenceService = personDetailsPersistenceService;
    }

    // Create new person
    public PersonDetails createPersonDetails(PersonDetails personDetails) {
        logger.info("Creating new PersonDetails for {}", personDetails.getFirstName());
        return personDetailsPersistenceService.save(personDetails);
    }

    // Get person by ID
    public PersonDetails getPersonDetailsById(int personDetailsId) {
        logger.info("Fetching PersonDetails by ID: {}", personDetailsId);
        if (personDetailsId <= 0) {
            logger.error("Invalid PersonDetails ID: {}", personDetailsId);
            throw new IllegalArgumentException("PersonDetails ID must be greater than zero");
        }
        return personDetailsPersistenceService.getPersonDetailsById(personDetailsId);
    }

    // Get all persons
    public List<PersonDetails> getAllPersonsDetails() {
        logger.info("Fetching all PersonDetails");
        return personDetailsPersistenceService.getAllPersonDetails();
    }

    // Update person details
    public PersonDetails updatePersonDetails(PersonDetails personDetails) {
        logger.info("Updating PersonDetails for ID: {}", personDetails.getPersonId());
        return personDetailsPersistenceService.updatePersonDetails(personDetails);
    }

    // Delete person by ID
    public boolean deletePersonDetails(int personDetailsId) {
        logger.info("Deleting PersonDetails by ID: {}", personDetailsId);
        if (personDetailsId <= 0) {
            logger.error("Invalid PersonDetails ID: {}", personDetailsId);
            throw new IllegalArgumentException("PersonDetails ID must be greater than zero");
        }
        try {
            boolean isDeleted = personDetailsPersistenceService.deletePersonDetails(personDetailsId);
            logger.info("Successfully deleted PersonDetails for ID: {}", personDetailsId);
            return isDeleted;
        } catch (Exception ex) {
            logger.error("Failed to delete PersonDetails for ID: {}", personDetailsId);
            throw ex;
        }
    }
}
