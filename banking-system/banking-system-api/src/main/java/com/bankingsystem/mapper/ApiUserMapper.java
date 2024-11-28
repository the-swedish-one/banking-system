package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiUser;
import com.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiUserMapper {

    // Map from service model User to API model ApiUser
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "person", target = "person")
    ApiUser toApiModel(User user);

    // Map from API model ApiUser to service model User
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "person", target = "person")
    User toServiceModel(ApiUser apiUser);

}