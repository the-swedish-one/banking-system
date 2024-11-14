package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiPersonDetails;
import com.bankingsystem.model.PersonDetails;
import org.springframework.stereotype.Component;

@Component
public class ApiPersonDetailsMapper {

    public ApiPersonDetails toApiModel(PersonDetails personDetails) {
        if (personDetails == null) return null;
        return new ApiPersonDetails(
                personDetails.getPersonId(),
                personDetails.getFirstName(),
                personDetails.getLastName(),
                personDetails.getEmail(),
                personDetails.getAddressLine1(),
                personDetails.getAddressLine2(),
                personDetails.getCity(),
                personDetails.getCountry()
        );
    }

    public PersonDetails toServiceModel(ApiPersonDetails apiPersonDetails) {
        if (apiPersonDetails == null) return null;
        return new PersonDetails(
                apiPersonDetails.getPersonId(),
                apiPersonDetails.getFirstName(),
                apiPersonDetails.getLastName(),
                apiPersonDetails.getEmail(),
                apiPersonDetails.getAddressLine1(),
                apiPersonDetails.getAddressLine2(),
                apiPersonDetails.getCity(),
                apiPersonDetails.getCountry()
        );
    }
}

