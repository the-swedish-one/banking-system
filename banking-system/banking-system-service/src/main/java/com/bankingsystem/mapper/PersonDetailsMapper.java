package com.bankingsystem.mapper;

import com.bankingsystem.model.PersonDetails;
import com.bankingsystem.model.PersonDetailsEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonDetailsMapper {

    public PersonDetails toModel(PersonDetailsEntity entity) {
        if (entity == null) return null;
        PersonDetails personDetails = new PersonDetails();
        personDetails.setPersonId(entity.getPersonId());
        personDetails.setFirstName(entity.getFirstName());
        personDetails.setLastName(entity.getLastName());
        personDetails.setEmail(entity.getEmail());
        personDetails.setAddressLine1(entity.getAddressLine1());
        personDetails.setAddressLine2(entity.getAddressLine2());
        personDetails.setCity(entity.getCity());
        personDetails.setCountry(entity.getCountry());
        return personDetails;
    }

    public PersonDetailsEntity toEntity(PersonDetails model) {
        if (model == null) return null;
        PersonDetailsEntity entity = new PersonDetailsEntity();
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        entity.setEmail(model.getEmail());
        entity.setAddressLine1(model.getAddressLine1());
        entity.setAddressLine2(model.getAddressLine2());
        entity.setCity(model.getCity());
        entity.setCountry(model.getCountry());
        return entity;
    }
}
