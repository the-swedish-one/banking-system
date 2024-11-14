package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiUser;
import com.bankingsystem.model.User;
import org.springframework.stereotype.Component;

@Component
public class ApiUserMapper {

    private final ApiPersonDetailsMapper apiPersonDetailsMapper;

    public ApiUserMapper(ApiPersonDetailsMapper apiPersonDetailsMapper) {
        this.apiPersonDetailsMapper = apiPersonDetailsMapper;
    }

    public ApiUser toApiModel(User user) {
        if (user == null) return null;
        ApiUser apiUser = new ApiUser();
        apiUser.setUserId(user.getUserId());
        apiUser.setPerson(apiPersonDetailsMapper.toApiModel(user.getPerson()));
        return apiUser;
    }

    public User toServiceModel(ApiUser apiUser) {
        if (apiUser == null) return null;
        User user = new User();
        user.setUserId(apiUser.getUserId());
        user.setPerson(apiPersonDetailsMapper.toServiceModel(apiUser.getPerson()));
        return user;
    }
}