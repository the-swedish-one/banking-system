package com.bankingsystem.api.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiUser {
    private Integer userId;
    private ApiPersonDetails person;

    public ApiUser(ApiPersonDetails person) {
        this.person = person;
    }
}
