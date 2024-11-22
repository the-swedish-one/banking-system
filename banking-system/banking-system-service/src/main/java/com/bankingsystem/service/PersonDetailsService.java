package com.bankingsystem.service;

import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.exception.PersonDetailsNotFoundException;
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
        PersonDetails savedPersonDetails = personDetailsPersistenceService.save(personDetails);
        logger.info("Successfully created PersonDetails with ID: {}", savedPersonDetails.getPersonId());
        return savedPersonDetails;
    }

    // Get person by ID
    public PersonDetails getPersonDetailsById(int personDetailsId) {
        logger.info("Fetching PersonDetails by ID: {}", personDetailsId);
        if (personDetailsId <= 0) {
            logger.error("Invalid PersonDetails ID: {}", personDetailsId);
            throw new IllegalArgumentException("PersonDetails ID must be greater than zero");
        }
        PersonDetails personDetails = personDetailsPersistenceService.getPersonDetailsById(personDetailsId);
        logger.info("Successfully fetched PersonDetails for ID: {}", personDetailsId);
        return personDetails;
    }

    // Get all persons
    public List<PersonDetails> getAllPersonsDetails() {
        logger.info("Fetching all PersonDetails");
        List<PersonDetails> personDetailsList = personDetailsPersistenceService.getAllPersonDetails();
        if (personDetailsList.isEmpty()) {
            logger.warn("No PersonDetails found in the database");
            throw new PersonDetailsNotFoundException("No person details found");
        }
        logger.info("Successfully fetched {} PersonDetails records", personDetailsList.size());
        return personDetailsList;
    }

    // Update person details
    public PersonDetails updatePersonDetails(PersonDetails personDetails) {
        logger.info("Updating PersonDetails for ID: {}", personDetails.getPersonId());
        PersonDetails updatedPersonDetails = personDetailsPersistenceService.updatePersonDetails(personDetails);
        logger.info("Successfully updated PersonDetails for ID: {}", updatedPersonDetails.getPersonId());
        return updatedPersonDetails;
    }

    // Delete person by ID
    public boolean deletePersonDetails(int personDetailsId) {
        logger.info("Deleting PersonDetails by ID: {}", personDetailsId);
        if (personDetailsId <= 0) {
            logger.error("Invalid PersonDetails ID: {}", personDetailsId);
            throw new IllegalArgumentException("PersonDetails ID must be greater than zero");
        }
        boolean isDeleted = personDetailsPersistenceService.deletePersonDetails(personDetailsId);
        if (isDeleted) {
            logger.info("Successfully deleted PersonDetails for ID: {}", personDetailsId);
        } else {
            logger.warn("Failed to delete PersonDetails for ID: {}", personDetailsId);
        }
        return isDeleted;
    }
}
