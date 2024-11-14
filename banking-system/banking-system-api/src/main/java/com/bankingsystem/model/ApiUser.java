package com.bankingsystem.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiUser {
    private int userId;
    private ApiPersonDetails person;

    public ApiUser(ApiPersonDetails person) {
        this.person = person;
    }
}
