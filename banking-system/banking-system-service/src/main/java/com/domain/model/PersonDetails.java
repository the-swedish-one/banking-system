package com.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetails {
    private int personId;
    private String firstName;
    private String lastName;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
}
