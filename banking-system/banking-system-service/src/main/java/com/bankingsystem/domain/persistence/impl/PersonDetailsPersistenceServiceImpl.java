package com.bankingsystem.domain.persistence.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bankingsystem.persistence.exception.PersonDetailsNotFoundException;
import com.bankingsystem.domain.mapper.PersonDetailsMapper;
import com.bankingsystem.domain.model.PersonDetails;
import com.bankingsystem.persistence.model.PersonDetailsEntity;
import com.bankingsystem.domain.persistence.PersonDetailsPersistenceService;
import com.bankingsystem.persistence.repository.PersonDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDetailsPersistenceServiceImpl implements PersonDetailsPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(PersonDetailsPersistenceServiceImpl.class);

    private final PersonDetailsRepository personDetailsRepository;
    private final PersonDetailsMapper personDetailsMapper;

    @Autowired
    public PersonDetailsPersistenceServiceImpl(PersonDetailsRepository personDetailsRepository, PersonDetailsMapper personDetailsMapper) {
        this.personDetailsRepository = personDetailsRepository;
        this.personDetailsMapper = personDetailsMapper;
    }

    // Create or save PersonDetails
    @Override
    public PersonDetails save(PersonDetails personDetails) {
        logger.info("Saving person details: {}", personDetails);
        PersonDetailsEntity entity = personDetailsMapper.toEntity(personDetails);
        PersonDetailsEntity savedEntity = personDetailsRepository.save(entity);
        logger.info("Successfully saved person details with ID: {}", savedEntity.getPersonId());
        return personDetailsMapper.toModel(savedEntity);
    }

    // Get a person details by ID
    @Override
    public PersonDetails getPersonDetailsById(int personDetailsId) {
        PersonDetailsEntity entity = personDetailsRepository.findById(personDetailsId)
                .orElseThrow(() -> {
                    logger.error("PersonDetails not found for ID: {}", personDetailsId);
                    return new PersonDetailsNotFoundException("PersonDetails not found");
                });
        logger.info("Successfully fetched person details with ID: {}", personDetailsId);
        return personDetailsMapper.toModel(entity);
    }

    // Get all Person Details
    @Override
    public List<PersonDetails> getAllPersonDetails() {
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll().stream()
                .map(personDetailsMapper::toModel)
                .collect(Collectors.toList());
        if (personDetailsList.isEmpty()) {
            logger.warn("No person details found");
            throw new PersonDetailsNotFoundException("No person details found");
        } else {
            logger.info("Successfully fetched {} person details", personDetailsList.size());
        }
        return personDetailsList;
    }

    // Update person details
    @Override
    public PersonDetails updatePersonDetails(PersonDetails personDetails) {
        PersonDetailsEntity existingEntity = personDetailsRepository.findById(personDetails.getPersonId())
                .orElseThrow(() -> {
                    logger.error("PersonDetails not found for ID: {}", personDetails.getPersonId());
                    return new PersonDetailsNotFoundException("PersonDetails not found");
                });

        // Update fields of the existing entity
        existingEntity.setFirstName(personDetails.getFirstName());
        existingEntity.setLastName(personDetails.getLastName());
        existingEntity.setEmail(personDetails.getEmail());
        existingEntity.setAddressLine1(personDetails.getAddressLine1());
        existingEntity.setAddressLine2(personDetails.getAddressLine2());
        existingEntity.setCity(personDetails.getCity());
        existingEntity.setCountry(personDetails.getCountry());

        // Save and map the updated entity back to the model
        PersonDetailsEntity updatedEntity = personDetailsRepository.save(existingEntity);
        logger.info("Successfully updated person details with ID: {}", personDetails.getPersonId());
        return personDetailsMapper.toModel(updatedEntity);
    }

    // Delete person
    @Override
    public boolean deletePersonDetails(int personDetailsId) {
        if (!personDetailsRepository.existsById(personDetailsId)) {
            logger.error("PersonDetails not found for ID: {}", personDetailsId);
            throw new PersonDetailsNotFoundException("PersonDetails not found");
        }
        personDetailsRepository.deleteById(personDetailsId);
        return true;
    }
}
