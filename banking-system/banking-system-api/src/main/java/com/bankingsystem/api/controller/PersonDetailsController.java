package com.bankingsystem.api.controller;

import com.bankingsystem.api.mapper.ApiPersonDetailsMapper;
import com.bankingsystem.api.model.ApiPersonDetails;
import com.bankingsystem.domain.model.PersonDetails;
import com.bankingsystem.domain.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person-details")
public class PersonDetailsController {

    private final PersonDetailsService personDetailsService;
    private final ApiPersonDetailsMapper apiPersonDetailsMapper;

    @Autowired
    public PersonDetailsController(PersonDetailsService personDetailsService, ApiPersonDetailsMapper apiPersonDetailsMapper) {
        this.personDetailsService = personDetailsService;
        this.apiPersonDetailsMapper = apiPersonDetailsMapper;
    }

    // Create a new person
    @PostMapping
    public ResponseEntity<ApiPersonDetails> createPersonDetails(@RequestBody ApiPersonDetails apiPersonDetails) {
        PersonDetails personDetails = apiPersonDetailsMapper.toServiceModel(apiPersonDetails);
        PersonDetails createdPerson = personDetailsService.createPersonDetails(personDetails);
        ApiPersonDetails createdApiPerson = apiPersonDetailsMapper.toApiModel(createdPerson);
        return new ResponseEntity<>(createdApiPerson, HttpStatus.CREATED);
    }

    // Get a person by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiPersonDetails> getPersonDetailsById(@PathVariable int id) {
        PersonDetails personDetails = personDetailsService.getPersonDetailsById(id);
        ApiPersonDetails apiPersonDetails = apiPersonDetailsMapper.toApiModel(personDetails);
        return new ResponseEntity<>(apiPersonDetails, HttpStatus.OK);
    }

    // Get all persons
    @GetMapping
    public ResponseEntity<List<ApiPersonDetails>> getAllPersonsDetails() {
        List<PersonDetails> personDetailsList = personDetailsService.getAllPersonsDetails();
        List<ApiPersonDetails> apiPersonDetailsList = personDetailsList.stream()
                .map(apiPersonDetailsMapper::toApiModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(apiPersonDetailsList, HttpStatus.OK);
    }

    // Update person details
    @PutMapping("/{id}")
    public ResponseEntity<ApiPersonDetails> updatePersonDetails(
            @PathVariable int id, @RequestBody ApiPersonDetails apiPersonDetails) {
        // Ensure the ID in the path matches the ID in the payload if needed
        apiPersonDetails.setPersonId(id);
        PersonDetails personDetails = apiPersonDetailsMapper.toServiceModel(apiPersonDetails);
        PersonDetails updatedPerson = personDetailsService.updatePersonDetails(personDetails);
        ApiPersonDetails updatedApiPerson = apiPersonDetailsMapper.toApiModel(updatedPerson);
        return new ResponseEntity<>(updatedApiPerson, HttpStatus.OK);
    }

    // Delete person by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonDetails(@PathVariable int id) {
        personDetailsService.deletePersonDetails(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
