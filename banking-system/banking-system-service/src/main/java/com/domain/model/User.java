package com.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private PersonDetails person;

    public User(PersonDetails person) {
        this.person = person;
    }
}
