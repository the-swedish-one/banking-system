package com.bankingsystem.api.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiPersonDetails {
    private Integer personId;
    private String firstName;
    private String lastName;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
}

