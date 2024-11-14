package com.bankingsystem.persistence.impl;

import com.bankingsystem.exception.PersonDetailsNotFoundException;
import com.bankingsystem.mapper.PersonDetailsMapper;
import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.model.PersonDetailsEntity;
import com.bankingsystem.persistence.PersonDetailsPersistenceService;
import com.bankingsystem.repository.PersonDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDetailsPersistenceServiceImpl implements PersonDetailsPersistenceService {

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
        PersonDetailsEntity entity = personDetailsMapper.toEntity(personDetails);
        PersonDetailsEntity savedEntity = personDetailsRepository.save(entity);
        return personDetailsMapper.toModel(savedEntity);
    }

    // Get a person details by ID
    @Override
    public PersonDetails getPersonDetailsById(int personDetailsId) {
        PersonDetailsEntity entity = personDetailsRepository.findById(personDetailsId)
                .orElseThrow(() -> new PersonDetailsNotFoundException("PersonDetails not found"));
        return personDetailsMapper.toModel(entity);
    }

    // Get all Person Details
    @Override
    public List<PersonDetails> getAllPersonDetails() {
        return personDetailsRepository.findAll().stream()
                .map(personDetailsMapper::toModel)
                .collect(Collectors.toList());
    }

    // Update person details
    @Override
    public PersonDetails updatePersonDetails(PersonDetails personDetails) {
        PersonDetailsEntity existingEntity = personDetailsRepository.findById(personDetails.getPersonId())
                .orElseThrow(() -> new PersonDetailsNotFoundException("PersonDetails not found"));

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
        return personDetailsMapper.toModel(updatedEntity);
    }

    // Delete person
    @Override
    public boolean deletePersonDetails(int personDetailsId) {
        if (personDetailsId <= 0) {
            throw new IllegalArgumentException("PersonDetails ID must be greater than zero");
        }
        if (!personDetailsRepository.existsById(personDetailsId)) {
            throw new PersonDetailsNotFoundException("PersonDetails not found");
        }
        personDetailsRepository.deleteById(personDetailsId);
        return true;
    }
}
