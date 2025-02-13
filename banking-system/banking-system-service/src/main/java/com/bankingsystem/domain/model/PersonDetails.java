package com.bankingsystem.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetails {
    private Integer personId;
    private String firstName;
    private String lastName;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
}
