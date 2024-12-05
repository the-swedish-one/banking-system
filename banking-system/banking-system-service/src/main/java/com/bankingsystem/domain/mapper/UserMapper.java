package com.bankingsystem.domain.mapper;

import com.bankingsystem.persistence.model.UserEntity;
import com.bankingsystem.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PersonDetailsMapper personDetailsMapper;

    public UserMapper(PersonDetailsMapper personDetailsMapper) {
        this.personDetailsMapper = personDetailsMapper;
    }

    public User toModel(UserEntity entity) {
        if (entity == null) return null;
        User user = new User();
        user.setUserId(entity.getUserId());
        user.setPerson(personDetailsMapper.toModel(entity.getPerson()));
        return user;
    }

    public UserEntity toEntity(User model) {
        if (model == null) return null;
        UserEntity entity = new UserEntity();
        if (model.getUserId() != null && model.getUserId() != 0) {
            entity.setUserId(model.getUserId());
        }
        entity.setPerson(personDetailsMapper.toEntity(model.getPerson()));
        return entity;
    }
}
