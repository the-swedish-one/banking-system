package com.bankingsystem.api.mapper;

import com.bankingsystem.api.model.ApiPersonDetails;
import com.bankingsystem.domain.model.PersonDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiPersonDetailsMapper {
    ApiPersonDetails toApiModel(PersonDetails personDetails);

    @Mapping(target = "personId", source = "personId")
    PersonDetails toServiceModel(ApiPersonDetails apiPersonDetails);
}

