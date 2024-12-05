package com.bankingsystem.api.mapper;

import com.bankingsystem.api.model.ApiUser;
import com.bankingsystem.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ApiPersonDetailsMapper.class})
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