package com.bankingsystem.api.mapper;

import com.bankingsystem.api.model.ApiPersonDetails;
import com.bankingsystem.domain.model.PersonDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiPersonDetailsMapper {

    // Map from service model PersonDetails to API model ApiPersonDetails
    ApiPersonDetails toApiModel(PersonDetails personDetails);

    // Map from API model ApiPersonDetails to service model PersonDetails
    PersonDetails toServiceModel(ApiPersonDetails apiPersonDetails);
}

