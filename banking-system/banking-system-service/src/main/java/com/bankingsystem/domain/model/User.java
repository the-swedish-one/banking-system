package com.bankingsystem.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private PersonDetails person;

    public User(PersonDetails person) {
        this.person = person;
    }
}
